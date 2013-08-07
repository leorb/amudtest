package org.leor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import java.io.InputStream;
import java.io.OutputStream;

public class AmudService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	void stream(InputStream input, OutputStream output) throws IOException {
		ReadableByteChannel inputChannel = null;
		WritableByteChannel outputChannel = null;

		try {
			inputChannel = Channels.newChannel(input);
			outputChannel = Channels.newChannel(output);
			ByteBuffer buffer = ByteBuffer.allocate(10240);
			while (inputChannel.read(buffer) != -1) {
				buffer.flip();
				outputChannel.write(buffer);
				buffer.clear();
			}
		} finally {
			if (outputChannel != null)
				try {
					outputChannel.close();
				} catch (IOException ignore) { /**/
				}
			if (inputChannel != null)
				try {
					inputChannel.close();
				} catch (IOException ignore) { /**/
				}
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			URL url = new URL("http://amudanan.co.il/services/ajax.php?"
					+ request.getQueryString());
			// response.setContentLength(url.getContentLength());
			stream(url.openStream(), response.getOutputStream());
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}
}
