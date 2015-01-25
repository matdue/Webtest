package testpackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

public class AsyncRequestProcessor implements Runnable {
	
	private AsyncContext asyncContext;
	
	public AsyncRequestProcessor() {
	}
	
	public AsyncRequestProcessor(AsyncContext asyncCtx) {
		asyncContext = asyncCtx;
	}

	@Override
	public void run() {
		doWork();
		
		ServletResponse response = asyncContext.getResponse();
		response.setContentType("text/plain");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("Nonblocking");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		asyncContext.complete();
	}
	
	private void doWork() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
