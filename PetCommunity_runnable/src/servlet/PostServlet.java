package servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 2132731135996613711L;
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		DiskFileItemFactory factory = new DiskFileItemFactory();
	    ServletFileUpload sfu = new ServletFileUpload(factory);
	    String twi = "";

		try {
				List<FileItem> items = sfu.parseRequest(request);
			     for(int i =0;i<items.size();i++){
			      FileItem item =items.get(i);
			      if(item.isFormField()){
			    	  String text = item.getString();
			    	  twi = twi + text;
			      }
			      if(!item.isFormField()) {
			       ServletContext context  = getServletContext();
			       String path  =context.getRealPath("upload");
			       File file = new File(path+"/pic_");
			       file.mkdir();
			       
			       String fileName1 = item.getName();
			       String fileName = fileName1.substring(fileName1.lastIndexOf("/")+1);
			       file = new File(path+"/"+"pic_"+"/"+fileName);
			       
			       item.write(file);
			       StatusUpdate status = new StatusUpdate(twi);
				   status.setMedia(file);
				   twitter.updateStatus(status);
			       
			      }	
			}
		} catch (TwitterException e) {
			 throw new ServletException(e);
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		response.sendRedirect("topics.jsp");
		
	}
}
