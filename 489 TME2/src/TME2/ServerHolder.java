package TME2;

/**
* TME2/ServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tme2.idl
* Tuesday, September 23, 2014 6:21:58 o'clock PM EDT
*/

public final class ServerHolder implements org.omg.CORBA.portable.Streamable
{
  public TME2.Server value = null;

  public ServerHolder ()
  {
  }

  public ServerHolder (TME2.Server initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = TME2.ServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    TME2.ServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TME2.ServerHelper.type ();
  }

}
