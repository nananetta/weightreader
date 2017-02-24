package com.metis.smartcardreader;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class ReaderThread implements Runnable {

	private CardTerminal terminal;

	public ReaderThread(CardTerminal terminal) {
		this.terminal = terminal;

	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(600);
				// establish a connection with the card
				Card card = null;
				try {
					card = terminal.connect("*");
				} catch (CardNotPresentException e) {
					System.out.println(e.getMessage());
					continue;
				}
//				System.out.println("card: " + card);

				// get the ATR
//				ATR atr = card.getATR();
//				byte[] baAtr = atr.getBytes();
//				System.out.print("ATR = 0x");
//				for (int i = 0; i < baAtr.length; i++) {
//					System.out.printf("%02X ", baAtr[i]);
//				}

				CardChannel channel = card.getBasicChannel();
				byte[] cmdApduGetCardUid = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00,	(byte) 0x00 };
				ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));

				if (respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00) {

					byte[] baCardUid = respApdu.getData();

					System.out.print("Card UID = 0x ");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < baCardUid.length; i++) {
						sb.append(String.format("%02X ", baCardUid[i]));
					}
					String cardUid = sb.toString();
					System.out.println(cardUid);
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
