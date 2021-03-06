package TME2;


/**
* TME2/ServerPOATie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from tme2.idl
* Tuesday, September 23, 2014 6:21:58 o'clock PM EDT
*/

public class ServerPOATie extends ServerPOA
{

  // Constructors

  public ServerPOATie ( TME2.ServerOperations delegate ) {
      this._impl = delegate;
  }
  public ServerPOATie ( TME2.ServerOperations delegate , org.omg.PortableServer.POA poa ) {
      this._impl = delegate;
      this._poa      = poa;
  }
  public TME2.ServerOperations _delegate() {
      return this._impl;
  }
  public void _delegate (TME2.ServerOperations delegate ) {
      this._impl = delegate;
  }
  public org.omg.PortableServer.POA _default_POA() {
      if(_poa != null) {
          return _poa;
      }
      else {
          return super._default_POA();
      }
  }
  public void registerFile (String filename, String clientname, String port)
  {
    _impl.registerFile(filename, clientname, port);
  } // registerFile

  public void removeFile (String filename, String clientname)
  {
    _impl.removeFile(filename, clientname);
  } // removeFile

  public boolean findFile (String filename)
  {
    return _impl.findFile(filename);
  } // findFile

  public String getFile (String filename)
  {
    return _impl.getFile(filename);
  } // getFile

  private TME2.ServerOperations _impl;
  private org.omg.PortableServer.POA _poa;

} // class ServerPOATie
