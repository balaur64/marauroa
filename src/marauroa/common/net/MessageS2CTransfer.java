/* $Id: MessageS2CTransfer.java,v 1.3 2005/04/14 09:59:07 quisar Exp $ */
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

import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;

public class MessageS2CTransfer extends Message
  {
  /** TODO: Compress all the data that we send */
  private List<TransferContent> contents;
  
  /** Constructor for allowing creation of an empty message */
  public MessageS2CTransfer()
    {
    super(MessageType.S2C_TRANSFER,null);
    }
  
  public MessageS2CTransfer(InetSocketAddress source,TransferContent content)
    {
    super(MessageType.S2C_TRANSFER,source);
    
    this.contents=new LinkedList<TransferContent>();
    contents.add(content);
    }
  
  public List<TransferContent> getContents()
    {
    return contents;
    }
  
  public String toString()
    {
    return "Message (S2C Transfer) from ("+source.getAddress().getHostAddress()+") CONTENTS: ("+contents.size()+")";
    }

  public void writeObject(marauroa.common.net.OutputSerializer out) throws IOException
    {
    super.writeObject(out);
    
    ByteArrayOutputStream array=new ByteArrayOutputStream();
    DeflaterOutputStream out_stream = new DeflaterOutputStream(array);
    OutputSerializer serializer=new OutputSerializer(out_stream);
        
    int size=contents.size();
    serializer.write(size);
    
    for(TransferContent content: contents)
      {
      content.writeFULL(serializer);
      }    

    out_stream.close();
         
    out.write(array.toByteArray());
    }
  
  public void readObject(marauroa.common.net.InputSerializer in) throws IOException, ClassNotFoundException
    {
    super.readObject(in);
    
    ByteArrayInputStream array=new ByteArrayInputStream(in.readByteArray());
    java.util.zip.InflaterInputStream szlib=new java.util.zip.InflaterInputStream(array,new java.util.zip.Inflater());
    InputSerializer serializer=new InputSerializer(szlib);

    int size=serializer.readInt();
    contents=new LinkedList<TransferContent>();
      
    for(int i=0;i<size;i++)
      {
      TransferContent content=new TransferContent();
      content.readFULL(serializer);
      contents.add(content);
      }

    if(type!=MessageType.S2C_TRANSFER)
      {
      throw new java.lang.ClassNotFoundException();
      }
    }
  }