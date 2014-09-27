package TME2;


/**
* TME2/ServerHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tme2.idl
* Tuesday, September 23, 2014 6:21:58 o'clock PM EDT
*/

abstract public class ServerHelper
{
  private static String  _id = "IDL:TME2/Server:1.0";

  public static void insert (org.omg.CORBA.Any a, TME2.Server that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static TME2.Server extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (TME2.ServerHelper.id (), "Server");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static TME2.Server read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ServerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, TME2.Server value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static TME2.Server narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TME2.Server)
      return (TME2.Server)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TME2._ServerStub stub = new TME2._ServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static TME2.Server unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof TME2.Server)
      return (TME2.Server)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      TME2._ServerStub stub = new TME2._ServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
