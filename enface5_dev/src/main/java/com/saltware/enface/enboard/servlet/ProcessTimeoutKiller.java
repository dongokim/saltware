package com.saltware.enface.enboard.servlet;

public class ProcessTimeoutKiller extends Thread {
	private Process process;
	private long timeout;
	
	public ProcessTimeoutKiller( Process process, long timeout) {
		this.process = process;
		this.timeout = timeout;
	}
	
	public void run() {
		try {
			sleep( timeout);
			if( process != null) {
				process.destroy();
			}
			
		} catch (Exception e) {
		}
	}
}
