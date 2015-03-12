
public class BuggyMethod1_acquireExclusiveReadLock {

	public boolean acquireExclusiveReadLock(GenericFileOperations<FTPFile> operations, GenericFile<FTPFile> file, Exchange exchange) throws Exception {
		boolean exclusive = false;

		LOG.trace("Waiting for exclusive read lock to file: " + file);

		long lastModified = Long.MIN_VALUE;
		long length = Long.MIN_VALUE;
		StopWatch watch = new StopWatch();

		while (!exclusive) {
			// timeout check
			if (timeout > 0) {
				long delta = watch.taken();
				if (delta > timeout) {
					CamelLogger.log(LOG, readLockLoggingLevel,
							"Cannot acquire read lock within " + timeout + " millis. Will skip the file: " + file);
					// we could not get the lock within the timeout period, so return false
					return false;
				}
			}

			long newLastModified = 0;
			long newLength = 0;
			List<FTPFile> files;
			if (fastExistsCheck) {
				// use the absolute file path to only pickup the file we want to check, this avoids expensive
				// list operations if we have a lot of files in the directory
				LOG.trace("Using fast exists to update file information for {}", file);
				files = operations.listFiles(file.getAbsoluteFilePath());
			} else {
				LOG.trace("Using full directory listing to update file information for {}. Consider enabling fastExistsCheck option.", file);
				// fast option not enabled, so list the directory and filter the file name
				files = operations.listFiles(file.getParent());
			}
			LOG.trace("List files {} found {} files", file.getAbsoluteFilePath(), files.size());
			for (FTPFile f : files) {
				if (f.getName().equals(file.getFileNameOnly())) {
					newLength = f.getSize();
					newLastModified = f.getTimestamp().getTimeInMillis();
				}
			}

			LOG.trace("Previous last modified: " + lastModified + ", new last modified: " + newLastModified);
			LOG.trace("Previous length: " + length + ", new length: " + newLength);

			if (length >= minLength && (newLastModified == lastModified && newLength == length)) {
				LOG.trace("Read lock acquired.");
				exclusive = true;
			} else {
				// set new base file change information
				lastModified = newLastModified;
				length = newLength;

				boolean interrupted = sleep();
				if (interrupted) {
					// we were interrupted while sleeping, we are likely being shutdown so return false
					return false;
				}
			}
		}

		return exclusive;
	}

	
	
}
