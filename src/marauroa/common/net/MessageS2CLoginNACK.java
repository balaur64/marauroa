/* $Id: MessageS2CLoginNACK.java,v 1.2 2005/04/14 09:59:07 quisar Exp $ */
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

/** This message indicate the client that the server has reject its login Message
 *  @see marauroa.common.net.Message
 */

public class MessageS2CLoginNACK extends Message
  {
  public final static byte UNKNOWN_REASON=0;
  public final static byte USERNAME_WRONG=1;
  public final static byte SERVER_IS_FULL=2;

  static private String[] text=
      {
    "Unknown reason",
    "Username/Password incorrect.",
    "Server is full.",
    "marauroa.common.network Protocol invalid version: Running "+Integer.toString(NetConst.NETWORK_PROTOCOL_VERSION)
    };
  private byte reason;

  /** Constructor for allowing creation of an empty message */
  public MessageS2CLoginNACK()
    {
    super(MessageType.S2C_LOGIN_NACK,null);
    }

  /** Constructor with a TCP/IP source/destination of the message
   *  @param source The TCP/IP address associated to this message
   *  @param resolution the reason to deny the login */
  public MessageS2CLoginNACK(InetSocketAddress source, byte resolution)
    {
    super(MessageType.S2C_LOGIN_NACK,source);
    reason=resolution;
    }

  /** This method returns the resolution of the login event
   *  @return a byte representing the resolution given.*/
  public byte getResolutionCode()
    {
    return reason;
    }

  /** This method returns a String that represent the resolution given to the login event
   *  @return a string representing the resolution.*/
  public String getResolution()
    {
    return text[reason];
    }

  /** This method returns a String that represent the object
   *  @return a string representing the object.*/
  public String toString()
    {
    return "Message (S2C Login NACK) from ("+source.getAddress().getHostAddress()+") CONTENTS: ("+getResolution()+")";
    }

  public void writeObject(marauroa.common.net.OutputSerializer out) throws IOException
    {
    super.writeObject(out);
    out.write(reason);
    }

  public void readObject(marauroa.common.net.InputSerializer in) throws IOException, java.lang.ClassNotFoundException
    {
    super.readObject(in);
    reason=in.readByte();
    if(type!=MessageType.S2C_LOGIN_NACK)
      {
      throw new java.lang.ClassNotFoundException();
      }
    }
  };