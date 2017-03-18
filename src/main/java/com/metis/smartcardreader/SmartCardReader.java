package com.metis.smartcardreader;

import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmartCardReader {

	private static final Logger LOGGER = LogManager.getLogger(SmartCardReader.class);
	private static CardTerminal terminal;
	
	public static void main(String[] args) {
		init();
		read();
	}
	
	public static void init() {
		try {
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals;
			terminals = factory.terminals().list();
			System.out.println("Terminals: " + terminals);
	
			// get the first terminal
			terminal = terminals.get(0);
		} catch (CardException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static SmartCard read() {
		
		Card card = null;
		try {
			// establish a connection with the card
			card = terminal.connect("*");
		
//			// get the ATR
//			ATR atr = card.getATR();
//			byte[] baAtr = atr.getBytes();
//			System.out.print("ATR = 0x");
//			for (int i = 0; i < baAtr.length; i++) {
//				System.out.printf("%02X ", baAtr[i]);
//			}

			CardChannel channel = card.getBasicChannel();
			byte[] cmdApduGetCardUid = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
			ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));

			if (respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00) {

				byte[] baCardUid = respApdu.getData();

				LOGGER.info("Card UID = 0x ");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < baCardUid.length; i++) {
					sb.append(String.format("%02X", baCardUid[i]));
				}
				String cardUid = sb.toString();
				LOGGER.info(cardUid);
				System.out.println(cardUid);
				return new SmartCard(cardUid);
			}
			return null;
		} catch (CardNotPresentException e) {
			return null;
		} catch (CardException e) {
			return null;
		} finally {
			// disconnect
			if(card != null) {
				try {
					card.disconnect(false);
				} catch (CardException e) {
					// do nothing
				}
			}
		}
	}

}
