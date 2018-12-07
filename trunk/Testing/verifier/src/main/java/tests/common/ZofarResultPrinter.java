package tests.common;

import java.util.Enumeration;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.textui.ResultPrinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZofarResultPrinter extends ResultPrinter {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarResultPrinter.class);

	public ZofarResultPrinter() {
		super(System.out);
	}
	
    protected void printHeader(long runTime) {
        getWriter().println();
//        getWriter().println("Time: " + elapsedTimeAsString(runTime));
    }
	
	@Override
    public void addError(Test test, Throwable t) {
//        getWriter().print("E");
    }

    @Override
    public void addFailure(Test test, AssertionFailedError t) {
//        getWriter().print("F");
    }
	
	@Override
    protected void printDefects(Enumeration<TestFailure> booBoos, int count, String type) {
        if (count == 0) return;
        getWriter().println("Errors found: " );
        for (int i = 1; booBoos.hasMoreElements(); i++) {
            printDefect(booBoos.nextElement(), i);
        }
    }
	
    @Override
    public void printDefect(TestFailure booBoo, int count) { // only public for testing purposes
        printDefectHeader(booBoo, count);
        getWriter().print(" ==> " );
        printDefectTrace(booBoo);
    }
	
	@Override
    protected void printDefectHeader(TestFailure booBoo, int count) {
        getWriter().print(count + ") " + booBoo.failedTest().getClass().getSimpleName());
    }

	@Override
    protected void printDefectTrace(TestFailure booBoo) {
        getWriter().print(booBoo.exceptionMessage());
    }
	@Override
    protected void printFooter(TestResult result) {
        if (result.wasSuccessful()) {
            getWriter().println();
            getWriter().print("OK");
            getWriter().println(" (" + result.runCount() + " test" + (result.runCount() == 1 ? "" : "s") + ")");

        }
        getWriter().println();
    }
    
	@Override
    public void startTest(Test test) {
//        getWriter().print(".");
//        if (fColumn++ >= 40) {
//            getWriter().println();
//            fColumn = 0;
//        }
    }

}
