/* $Id: IRPRuleProcessor.java,v 1.4 2005/04/06 15:34:59 arianne_rpg Exp $ */
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
package marauroa.server.game;

import java.util.*;
import marauroa.common.game.*;

/** Interface for the class that is in charge of executing actions.
 *  Implement it to personalize the game */
public interface IRPRuleProcessor
  {
  /** Set the context where the actions are executed.
   *  @param zone The zone where actions happens. */
  public void setContext(RPServerManager rpman,RPWorld world);
  
  /** This method is called *before* adding an action by RPScheduler so you can 
   *  choose not to allow the action to be added by returning false */
  public boolean onActionAdd(RPAction action, List<RPAction> actionList);
  /** This method is called *before* adding an incomplete action, an action that 
   *  has been added before but has not been completed, by RPScheduler so you can 
   *  choose not to allow the action to be added by returning false */
  public boolean onIncompleteActionAdd(RPAction action, List<RPAction> actionList);
  
  /** Execute an action in the name of a player.
   *  @param id the id of the object owner of the actions.
   *  @param action the action to execute
   *  @return the action status, that can be Success, Fail or incomplete, please
   *      refer to Actions Explained for more info. */
  public RPAction.Status execute(RPObject.ID id, RPAction action);
  
  /** Notify it when a begin of actual turn happens. */
  public void beginTurn();
  /** Notify it when a end of actual turn happens. */
  public void endTurn();
  
  /** Callback method called when a new player enters in the game
   *  @param object the new player that enters in the game. */
  public boolean onInit(RPObject object) throws RPObjectInvalidException;
  
  /** Callback method called when a new player exits the game
   *  @param id the new player id that exits the game.
   *  @return true to update the player on database. */
  public boolean onExit(RPObject.ID id) throws RPObjectNotFoundException;
  
  /** Callback method called when a new player time out
   *  @param id the new player id that timeouts. */
  public boolean onTimeout(RPObject.ID id) throws RPObjectNotFoundException;
  }