/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.semaphoro;
import java.util.LinkedList;

/**
 * jCompany. Classe utilitária para manter singletons de caching
 *      thread-safetey, controlando concorrência 
 *		no momento da atualização e permitindo concorrência nas leituras
 * Baseado em algoritmo testado em sites de larga escala.
 * @since jCompany 3.0
*/
public class PlcReaderWriter {
	
    private int active_readers;     // = 0
    private int waiting_readers;    // = 0
    private int active_writers;     // = 0

	/**   
	I keep a linked list of writers waiting for access so that I can
	release them in the order that the requests were received.  The size of
	this list is the "waiting writers" count.  Note that the monitor of the
	PlcLeitorGravador object itself is used to lock out readers
	while writes are in progress, thus there's no need for a separate
	"reader_lock."
	*/
	private final LinkedList writer_locks = new LinkedList();

	/**
	Request the read lock. Block until a read operation can be performed
	safely.  This call must be followed by a call to
	read_accomplished() when the read operation completes.
	*/
	public synchronized void leitura(){

		if( active_writers==0 && writer_locks.size()==0 )
			++active_readers;
		else
		{   ++waiting_readers;
			try{ wait(); }catch(InterruptedException e){}
		}
	}

	/**
	This version of leitura() requests read access and returns
	true if you get it. If it returns false, you may not
	safely read from the guarded resource. If it returns true, you
	should do the read, then call read_accomplished in the
	normal way. Here's an example:

		public void leitura()
		{   if( lock.request_immediate_leitura() )
			{   try
				{
					// do the read operation here
				}
				finally
				{   lock.leituraCompleta();
				}
			}
			else
				// couldn't read safely.
		}
	*/
    public synchronized boolean request_immediate_leitura()
    {
        if( active_writers==0 && writer_locks.size()==0 )
        {   ++active_readers;
            return true;
        }
        return false;
    }

	/**   
	Release the lock. You must call this method when you're done
	with the read operation.
	*/
    public synchronized void leituraCompleta()
    {   if( --active_readers == 0 )
            notify_writers();
    }

	/** 
	Request the write lock. Block until a write operation can be performed
	safely. Write requests are guaranteed to be executed in the order
	received. Pending read requests take precedence over all write
	requests.  This call must be followed by a call to
	gravacaoCompleta() when the write operation completes.
	*/
    public void gravacao()
    {
        // This method can't be synchronized or there'd be a nested-monitor
        // lockout problem: We have to acquire the lock for "this" in
        // order to modify the fields, but that lock must be released
        // before we start waiting for a safe time to do the writing.
        // If request_gravacao() were synchronized, we'd be holding
        // the monitor on the PlcLeitorGravador lock object while we were
        // waiting. Since the only way to be released from the wait is
        // for someone to call either read_accomplished()
        // or gravacaoCompleta() (both of which are synchronized),
        // there would be no way for the wait to terminate.

        Object lock = new Object();
        synchronized( lock )
        {   synchronized( this )
            {   boolean okay_to_write = writer_locks.size()==0 
                                        && active_readers==0
                                        && active_writers==0;
                if( okay_to_write )
                {   ++active_writers;
                    return; // the "return" jumps over the "wait" call
                }

                writer_locks.addLast( lock );
            }
            try{ lock.wait(); } catch(InterruptedException e){}
        }
    }

	/**    
	This version of the write request returns false immediately
	(without blocking) if any read or write operations are in progress and
	a write isn't safe; otherwise, it returns true and acquires the
	resource. Use it like this:

		public void gravacao()
		{   if( lock.request_immediate_gravacao() )
			{   try
				{
					// do the write operation here
				}
				finally
				{   lock.gravacaoCompleta();
				}
			}
			else
				// couldn't write safely.
		}
	*/
	synchronized public boolean request_immediate_gravacao() {
        if( writer_locks.size()==0  && active_readers==0
                                    && active_writers==0 )
        {   ++active_writers;
            return true;
        }
        return false;
    }

	/**      
	Release the lock. You must call this method when you're done
	with the read operation.
	*/
	public synchronized void gravacaoCompleta()  {
        // The logic here is more complicated than it appears.
        // If readers have priority, you'll  notify them. As they
        // finish up, they'll call read_accomplished(), one at
        // a time. When they're all done, read_accomplished() will
        // notify the next writer. If no readers are waiting, then
        // just notify the writer directly.

        --active_writers;
        if( waiting_readers > 0 )   // priority to waiting readers
            notify_readers();
        else
            notify_writers();
    }

    /**
	Notify all the threads that have been waiting to read.
    */
    private void notify_readers()       // must be accessed from a
    {                                   //  synchronized method
        active_readers  += waiting_readers;
        waiting_readers = 0;
        notifyAll();
    }

    /**   
	 Notify the writing thread that has been waiting the longest.
     */
    private void notify_writers()       // must be accessed from a
    {                                   //  synchronized method
        if( writer_locks.size() > 0 )
        {   
            Object oldest = writer_locks.removeFirst();
            ++active_writers;
            synchronized( oldest ){ oldest.notify(); }
        }
    }
 
}
 
