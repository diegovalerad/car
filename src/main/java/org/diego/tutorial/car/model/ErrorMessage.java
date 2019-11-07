package org.diego.tutorial.car.model;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents an error message. This error is used by the exceptions to 
 * simplify its use. 
 * <p>
 * Each error message, will have 3 fields:
 * <ul>
 * <li>An error message, that provides information about the error.</li>
 * <li>An error code, that provides the {@link Status} error.</li>
 * <li>A documentation, that provides additional information.</li>
 * </ul>
 */
@XmlRootElement
public class ErrorMessage {
	private String errorMessage;
	private int errorCode;
	private String documentation;
	
	public ErrorMessage() {
	}
	
	public ErrorMessage(String errorMessage, int errorCode, String documentation) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

}
