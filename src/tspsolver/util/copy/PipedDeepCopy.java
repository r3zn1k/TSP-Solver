package tspsolver.util.copy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects in a memory efficient way. Objects are serialized in the calling thread and de-serialized in another thread.
 * 
 * Error checking is fairly minimal in this implementation. If an object is encountered that cannot be serialized (or that references an object that cannot be serialized) an error is printed to System.err and null is returned. Depending on your specific
 * application, it might make more sense to have copy(...) re-throw the exception.
 */
public class PipedDeepCopy {

	/**
	 * Flag object used internally to indicate that deserialization failed.
	 */
	private static Object ERROR = new Object();

	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Make a connected pair of piped streams
			PipedInputStream in = new PipedInputStream();
			PipedOutputStream pos = new PipedOutputStream(in);

			// Make a deserializer thread (see inner class below)
			Deserializer des = new Deserializer(in);

			// Write the object to the pipe
			ObjectOutputStream out = new ObjectOutputStream(pos);
			out.writeObject(orig);
			out.close();

			// Wait for the object to be deserialized
			obj = des.getDeserializedObject();

			// See if something went wrong
			if (obj == PipedDeepCopy.ERROR) {
				obj = null;
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return obj;
	}

	/**
	 * Thread subclass that handles deserializing from a PipedInputStream.
	 */
	private static class Deserializer extends Thread {
		/**
		 * Object that we are deserializing
		 */
		private Object obj = null;

		/**
		 * Lock that we block on while deserialization is happening
		 */
		private Object lock = null;

		/**
		 * InputStream that the object is deserialized from.
		 */
		private PipedInputStream in = null;

		public Deserializer(PipedInputStream pin) throws IOException {
			this.lock = new Object();
			this.in = pin;
			this.start();
		}

		@Override
		public void run() {
			Object o = null;
			try {
				ObjectInputStream oin = new ObjectInputStream(this.in);
				o = oin.readObject();
			}
			catch (IOException e) {
				// This should never happen. If it does we make sure
				// that a the object is set to a flag that indicates
				// deserialization was not possible.
				e.printStackTrace();
			}
			catch (ClassNotFoundException cnfe) {
				// Same here...
				cnfe.printStackTrace();
			}

			synchronized (this.lock) {
				if (o == null) {
					this.obj = PipedDeepCopy.ERROR;
				}
				else {
					this.obj = o;
				}
				this.lock.notifyAll();
			}
		}

		/**
		 * Returns the deserialized object. This method will block until the object is actually available.
		 */
		public Object getDeserializedObject() {
			// Wait for the object to show up
			try {
				synchronized (this.lock) {
					while (this.obj == null) {
						this.lock.wait();
					}
				}
			}
			catch (InterruptedException ie) {
				// If we are interrupted we just return null
			}
			return this.obj;
		}
	}

}