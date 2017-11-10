package fr.aeris.actris.validation.rest.services.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class ActrisFtpClient implements AutoCloseable{

	private FtpConfig ftpConfig;
	private FTPClient client;

	public ActrisFtpClient(FtpConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}
	
	public void connect() throws Exception {
		if (client == null) {
			client = new FTPClient();
			client.connect(ftpConfig.getAddress());
			client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			boolean connexionResult = client.login(ftpConfig.getLogin(), ftpConfig.getPassword());
			if (connexionResult ==  false) {
				throw new Exception("Ftp provider: connexion impossible");
			}
		}
	}
	
	public boolean directoryExists(String directoryName) throws Exception {
		String aux = ftpConfig.getRootFolder()+"/"+directoryName;
		aux = aux.replace("//", "/");
		boolean changeWorkingDirectory = client.changeWorkingDirectory(aux);
		return changeWorkingDirectory;
	}
	
	public void createDirectory(String directoryName) throws Exception {
		if (directoryExists(directoryName) == false) {
			client.mkd(directoryName);
		}
	}
	
	public File downloadFile(FTPFile ftpFile, String distantDirectoryName, File localDirectory) throws Exception {
		return downloadFile(ftpFile, distantDirectoryName, localDirectory, ftpFile.getName());
	}
	
	public void uploadFile(String fileName, InputStream stream, String directoryName) throws Exception {
		client.changeWorkingDirectory(directoryName);
		client.storeFile(fileName, stream);
		IOUtils.closeQuietly(stream);
		
		 boolean commandOK = client.completePendingCommand();
		    if (!commandOK) {
		    	throw new Exception("An error has occured while transfering file");
		    }
	}
	
	public File downloadFile(FTPFile ftpFile, String distantDirectoryName, File localDirectory, String localFileName) throws Exception {
		
		if (ftpFile.isFile()) {
			return downloadFile(ftpFile.getName(), distantDirectoryName, localDirectory, localFileName);
		}
		else {
			throw new Exception("Impossible to download a distant directory");
		}		
		
	}
	
	public File downloadFile(String distantFileName, String distantDirectoryName, File localDirectory, String localFileName) throws Exception {
		
		
			client.changeWorkingDirectory(distantDirectoryName);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			//Create an InputStream to the File Data and use FileOutputStream to write it
		    InputStream inputStream = client.retrieveFileStream(distantFileName);
		    File result = new File(localDirectory, distantFileName);
		    FileOutputStream fileOutputStream = new FileOutputStream(result);
		    //Using org.apache.commons.io.IOUtils
		    IOUtils.copy(inputStream, fileOutputStream);
		    fileOutputStream.flush();
		    IOUtils.closeQuietly(fileOutputStream);
		    IOUtils.closeQuietly(inputStream);
		    boolean commandOK = client.completePendingCommand();
		    if (!commandOK) {
		    	throw new Exception("An error has occured while transfering file");
		    }
		    else {
		    	return result;
		    }
		
				
		
	}
	
	
	public List<FTPFile> listDirectory(String directoryName) throws Exception {
		
		String aux = ftpConfig.getRootFolder()+"/"+directoryName;
		aux = aux.replace("//", "/");
		boolean changeWorkingDirectory = client.changeWorkingDirectory(aux);
		if (changeWorkingDirectory == true) {
			FTPFile[] listFiles = client.listFiles();
			return Arrays.asList(listFiles);
		}
		else {
			return new ArrayList<>();
		}
		
	}
	
	

	@Override
	public void close() throws Exception {
		if (client != null) {
			client.disconnect();
			client = null;
		}
		
	}

	public void deleteFile(String ftpFolder, String fileName) throws Exception {
		boolean changeWorkingDirectory = client.changeWorkingDirectory(ftpFolder);
		if (changeWorkingDirectory) {
			FTPFile[] listFiles = client.listFiles();
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.getName().toLowerCase().equals(fileName.toLowerCase())) {
					client.deleteFile(ftpFile.getName());
				}
			}
			
		}
	}

}