package com.vidhyalearning.myquizapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLText extends AdminActivity.PlaceholderFragment{
EditText xmlText;
    MyDBHelper mydb;
Button submitBtn;
    public XMLText() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static XMLText newInstance() {
        String param1="",param2="";
        XMLText fragment = new XMLText();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mydb = new MyDBHelper(getActivity(),"MyDBName.db",null,MyDBHelper.DB_VERSION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_xmltext, container, false);
        xmlText = (EditText) view.findViewById(R.id.xmlText);
        submitBtn = (Button)view.findViewById(R.id.submitBtn);
            submitBtn.setOnClickListener(new View.OnClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                        wrapperUploadQuestions();

                }

        });
        return view;
    }
   @RequiresApi(api = Build.VERSION_CODES.M)
   void wrapperUploadQuestions(){
       try {
           uploadQuestions();
       } catch (ParserConfigurationException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (SAXException e) {
           e.printStackTrace();
       }
   }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void uploadQuestions() throws ParserConfigurationException, IOException, SAXException {
        String strXMLtext = xmlText.getText().toString().trim();
        if(strXMLtext.isEmpty())
            return;
try{

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(strXMLtext)));
        Element element=doc.getDocumentElement();
        element.normalize();

        NodeList nList = doc.getElementsByTagName("qaset");
        Vector<String> subjectList = new Vector<String>();
        subjectList.clear();
        for (int i=0; i<nList.getLength(); i++) {


            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element2 = (Element) node;
                String question = getValue("question", element2);
                String answer =getValue("answer", element2);
                String level = getValue("level", element2);
                String subject = getValue("subject", element2);
                questionAnswer qa = new questionAnswer(question,answer,level,subject,null);
                if((subjectList.isEmpty() || subjectList.contains(subject) == false))
                {
                    mydb.insertCategory(subject,null);
                    subjectList.add(subject);
                }
                mydb.insertQA(qa);
            }
        }
    Toast.makeText(getActivity(),"Upload Successful" ,Toast.LENGTH_SHORT).show();
    xmlText.setText("");

    } catch(Exception e)
{
    e.printStackTrace();
    Toast.makeText(getActivity(),"Some Error\n" + e.getMessage() ,Toast.LENGTH_SHORT).show();
}
}
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
