package com.xuwt.xmlparser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuwt on 2014/12/2.
 *
 * parser xml tools
 */
public class StackOverflowXmlParser {
    //xml namespace
    private static  final String NAMESPACE=null;
    private static final String FEED="feed";
    private static final String ENTRY="entry";
    private static final String TITLE="title";
    private static final String SUMMARY="summary";
    private static final String LINK="link";
    private static final String REL="rel";
    private static final String HREF="href";
    private static final String ALTERNATE ="alternate" ;


    public List<Entry> parse(InputStream in) throws XmlPullParserException,IOException{

        try {
            XmlPullParser parser= Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);
            parser.nextTag();
            return readFeed(parser); 
        }finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException,IOException{
        List<Entry> entries=new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG,NAMESPACE,FEED);
        while (parser.next()!=XmlPullParser.END_TAG){
            if(parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String name=parser.getName();

            if(name.equals(ENTRY)){
                entries.add(readEntry(parser));
            }else{
                skip(parser);
            }

        }

        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException,IOException{
        if(parser.getEventType()!=XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }
        int depth=1;
        while (depth!=0){
            switch (parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException,IOException{
        parser.require(XmlPullParser.START_TAG,NAMESPACE,ENTRY);
        String title=null;
        String summary=null;
        String link=null;

        while(parser.next()!=XmlPullParser.END_TAG){
            if(parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String name=parser.getName();
            if(name.equals(TITLE)){
                title=readTitle(parser);
            }else if(name.equals(SUMMARY)){
                summary=readSummary(parser);
            }else if(name.equals(LINK)){
                link=readLink(parser);
            }else{
                skip(parser);
            }

        }
        return new Entry(title,link,summary);

    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException,IOException{
        String link="";
        parser.require(XmlPullParser.START_TAG,NAMESPACE,LINK);

        String tag=parser.getName();
        String relType=parser.getAttributeValue(null,REL);
        if(tag.equals(LINK)||relType.equals(ALTERNATE)){
            link=parser.getAttributeValue(null,HREF);
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG,NAMESPACE,LINK);
        return link;
    }

    private String readSummary(XmlPullParser parser) throws XmlPullParserException,IOException{
        parser.require(XmlPullParser.START_TAG,NAMESPACE,SUMMARY);
        String summary=readText(parser);
        parser.require(XmlPullParser.END_TAG,NAMESPACE,SUMMARY);
        return summary;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException,IOException{
        String result="";
        if(parser.next()==XmlPullParser.TEXT){
            result=parser.getText();
            parser.nextTag();
        }
        return  result;
    }


    private String readTitle(XmlPullParser parser) throws XmlPullParserException,IOException{
        parser.require(XmlPullParser.START_TAG,NAMESPACE,TITLE);
        String title=readText(parser);
        parser.require(XmlPullParser.END_TAG,NAMESPACE,TITLE);
        return title;
    }




}
