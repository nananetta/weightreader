package com.metis.smartcardreader;

import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class SmartCardReader {

	public static void main(String[] args) {
		SmartCardReader cc = new SmartCardReader();
		cc.read();
	}

	public void read() {
		try {
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();
			System.out.println("Terminals: " + terminals);

			// get the first terminal
			CardTerminal terminal = terminals.get(0);
			
			ReaderThread rt = new ReaderThread(terminal);
			rt.run();
			// establish a connection with the card
//			Card card = terminal.connect("*");
//			System.out.println("card: " + card);
//			
//			// get the ATR
//			ATR atr = card.getATR();
//			byte[] baAtr = atr.getBytes();
//			System.out.print("ATR = 0x");
//			for (int i = 0; i < baAtr.length; i++) {
//				System.out.printf("%02X ", baAtr[i]);
//			}
//
//			System.out.println();
//			CardChannel channel = card.getBasicChannel();
//			byte[] cmdApduGetCardUid = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
//			ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));
//
//			if (respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00) {
//
//				byte[] baCardUid = respApdu.getData();
//
//				System.out.print("Card UID = 0x");
//				for (int i = 0; i < baCardUid.length; i++) {
//					System.out.printf("%02X ", baCardUid[i]);
//				}
//			}
			// disconnect
//			card.disconnect(false);
//			rt.wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
