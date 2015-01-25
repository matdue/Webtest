package testpackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Nonblocking
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Nonblocking" })
public class Nonblocking extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Nonblocking() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext asyncCtx = request.startAsync();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext().getAttribute("executor");
		try {
			// executor.execute(new AsyncRequestProcessor(asyncCtx));
			executor.execute(() -> {
				ServletResponse asyncResponse = asyncCtx.getResponse();
				if (asyncResponse == null) {
					return;
				}
				
				asyncResponse.setContentType("text/plain");
				try (PrintWriter out = asyncResponse.getWriter()) {
					out.println("Nonblocking");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				asyncCtx.complete();
			});
		} catch (RejectedExecutionException e) {
			// Executor has no capacity left for further tasks
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("text/plain");
			try (PrintWriter out = response.getWriter()) {
				out.println("Nonblocking");
			}
			asyncCtx.complete();
		}
	}

}
