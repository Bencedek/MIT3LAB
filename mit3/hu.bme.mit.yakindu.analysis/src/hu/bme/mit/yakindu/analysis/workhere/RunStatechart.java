package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
	    s.runCycle();
	    
	    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
		    String input = bfr.readLine();
		    switch (input) {
		        case "start":
		            s.raiseStart();
				    s.runCycle();
				    break;
		        case "white":
		            s.raiseWhite();
				    s.runCycle();
				    break;
		        case "black":
		            s.raiseBlack();
				    s.runCycle();
				    break;
		        case "exit":
		            System.exit(0);
		    }
		    print(s);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
