/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author lykia
 */
public class MultiThreadApp
{
    public static SchedulingAlgorithms[] threads = 
            new SchedulingAlgorithms[7];
    
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException
    {
        for(int i = 0; i < 7; i++)
        {
            threads[i] = new SchedulingAlgorithms(i);
            threads[i].start();
        }
        
        for(int i = 0; i < 7; i++)
            threads[i].join();
        
        
        for(int i = 1; i < 8; i++)
        {
            BufferedReader in = new BufferedReader(new FileReader("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/" + i + ".txt"));
            String line;
        
            while((line = in.readLine()) != null)
                System.out.println(line);
            
            System.out.println();
            in.close();
        }
    }
}