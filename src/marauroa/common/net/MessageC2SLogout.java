/* $Id: MessageC2SLogout.java,v 1.2 2005/04/14 09:59:06 quisar Exp $ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package marauroa.common.net;
  
import java.net.InetSocketAddress;
import java.io.*;

/** The Logout Message is sent from client to server to indicate that it wants to 
 *  finish the session. */
public class MessageC2SLogout extends Message
  { 
  /** Constructor for allowing creation of an empty message */
  public MessageC2SLogout()
    {
    super(MessageType.C2S_LOGOUT,null);
    }
    
  /** Constructor with a TCP/IP source/destination of the message and the name
   *  of the choosen character.
   *  @param source The TCP/IP address associated to this message
   */
  public MessageC2SLogout(InetSocketAddress source)
    {
    super(MessageType.C2S_LOGOUT,source);
    }  
  
  /** This method returns a String that represent the object 
   *  @return a string representing the object.*/
  public String toString()
    {
    return "Message (C2S Logout) from ("+source.getAddress().getHostAddress()+") CONTENTS: ()";
    }
      
  public void writeObject(marauroa.common.net.OutputSerializer out) throws IOException
    {
    super.writeObject(out);
    }
    
  public void readObject(marauroa.common.net.InputSerializer in) throws IOException, java.lang.ClassNotFoundException
    {
    super.readObject(in);
    if(type!=MessageType.C2S_LOGOUT)
      {
      throw new java.lang.ClassNotFoundException();
      }
    }    
  }


;