package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.StringTokenizer;

import static java.lang.Double.parseDouble;

public class MainActivity extends Activity {

    TextView formulaStr, resultStr;
    RadioGroup radioGroup;
    int checkedRadioBtn;
    String decimalText = "";
    String heximalText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         formulaStr=(TextView) findViewById(R.id.formula);
         resultStr=(TextView) findViewById(R.id.result);
         radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
         radioGroup.check(R.id.DecimalRB);
         checkedRadioBtn=radioGroup.getCheckedRadioButtonId();

    }

    private int order(String op){
        if(op.equals("(")) return 0;
        if(op.equals("+")|| op.equals("-")) return 1;
        if(op.equals("X")|| op.equals("/")) return 2;
        else return 3;
    }
    private String calc(String formula) {
        Stack<String> postfix=new Stack<>();
        Stack<String> operator=new Stack<>();
        StringTokenizer st=new StringTokenizer(formula,"+-X/()",true);
        while(st.hasMoreTokens()){
            String str = st.nextToken();
            if("+-X/".contains(str)){
                if(operator.isEmpty()){
                    operator.push(str);
                }
                else{
                    if(order(operator.peek()) >= order(str)) {
                        postfix.push(operator.peek());
                        operator.pop();
                        operator.push(str);
                    }
                    else{
                        operator.push(str);
                    }
                }
            }
            else if(str.equals("(")){
               operator.push(str);
            }
            else if(str.equals(")")){
                while(!operator.peek().equals("(")){
                    postfix.push(operator.peek());
                    operator.pop();
                }
                operator.pop();
            }
            else{
                postfix.push(str);
            }
        }

        while(!operator.isEmpty()){
            postfix.push(operator.peek());
            operator.pop();
        }

        operator.clear();

        int size = postfix.size();

        for(int i = 0 ;i<size ;i++){
            operator.push(postfix.peek());
            postfix.pop();
        }

        postfix=operator;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Stack<String> temp=new Stack<>();

        for(int i = 0 ;i<size ;i++){
            String s = postfix.peek();
            if(s.equals("+")) {
                double num1 = Double.parseDouble(temp.peek());
                temp.pop();
                double num2 = Double.parseDouble(temp.peek());
                temp.pop();
                temp.push(decimalFormat.format(num2 + num1));
            }
            else if(s.equals("-")) {
                double num1 = Double.parseDouble(temp.peek());
                temp.pop();
                double num2 = Double.parseDouble(temp.peek());
                temp.pop();
                temp.push(decimalFormat.format(num2 - num1));
            }
            else if(s.equals("X")) {
                double num1 = Double.parseDouble(temp.peek());
                temp.pop();
                double num2 = Double.parseDouble(temp.peek());
                temp.pop();
                temp.push(decimalFormat.format(num2 * num1));
            }
            else if(s.equals("/")) {
                double num1 = Double.parseDouble(temp.peek());
                temp.pop();
                double num2 = Double.parseDouble(temp.peek());
                temp.pop();
                temp.push(decimalFormat.format(num2 / num1));
            }
            else {
                temp.push(s);
            }
            postfix.pop();
        }

        return temp.peek();
    }

    public void RadioBtnClick(View v){
        checkedRadioBtn= radioGroup.getCheckedRadioButtonId();
        if(checkedRadioBtn==R.id.DecimalRB) {
            resultStr.setText(decimalText);
        }
        else if(checkedRadioBtn==R.id.HeximalRB){
            resultStr.setText(heximalText);
        }
    }
    public void onBtnClick(View v){
        String tempStr=formulaStr.getText().toString();
        Button btn = (Button)v;
        switch(btn.getText().toString()){
            case "←":
                if(tempStr.length()>0 && resultStr.getText()=="")
                formulaStr.setText(tempStr.substring(0,tempStr.length()-1));
                else if(resultStr.getText()!=""){
                    formulaStr.setText("");
                    resultStr.setText("");
                    decimalText = "";
                    heximalText = "";
                }
                break;
            case "=":
                decimalText = calc(tempStr).toUpperCase();
                heximalText = Integer.toHexString((int)Math.floor(Double.parseDouble(calc(tempStr)))).toUpperCase();

                if(checkedRadioBtn==R.id.DecimalRB) {
                    resultStr.setText(decimalText);
                }
                else if(checkedRadioBtn==R.id.HeximalRB){
                    resultStr.setText(heximalText);
                }
                break;
            default:
                formulaStr.setText(tempStr+btn.getText().toString());
                break;
        }
    }
}
