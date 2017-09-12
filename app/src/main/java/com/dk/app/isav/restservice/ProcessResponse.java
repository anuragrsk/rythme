package com.dk.app.isav.restservice;



import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProcessResponse
{
  private static String getCharacterDataFromElement(Node paramNode)
  {
    Node localNode = paramNode.getFirstChild();
    if ((localNode instanceof CharacterData))
      return ((CharacterData)localNode).getData();
    return "?";
  }

  private static void parseDoc(Node paramNode, Map paramMap)
  {
    if (paramNode.hasChildNodes())
    {
      NodeList localNodeList = paramNode.getChildNodes();
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        Node localNode = localNodeList.item(i);
        if (!"?".equals(getCharacterDataFromElement(localNode)))
          paramMap.put(localNode.getNodeName(), getCharacterDataFromElement(localNode));
        parseDoc(localNode, paramMap);
      }
    }
  }

  private static void printHashMap(HashMap paramHashMap)
  {
    Iterator localIterator = paramHashMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Log.d("Print Hashmap", "The key is map1: " + localEntry.getKey() + ",value is :" + localEntry.getValue());
    }
  }

  public static Object process(String paramString, Object paramObject)
  {
    Gson localGson = new Gson();
    Log.d("ProcessResponse::response:::::::::", paramString.length() + "");
    return localGson.fromJson(paramString, paramObject.getClass());
  }
}