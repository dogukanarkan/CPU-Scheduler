/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadapp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lykia
 */
public class SchedulingAlgorithms extends Thread implements Comparable<Process>
{
    private final int algorithmNumber;
    private List<String> text = new ArrayList<>();
    private final ArrayList<Process> tempProcessList = new ArrayList<>();
    
    public SchedulingAlgorithms(int algorithmNumber) throws IOException
    {
        this.algorithmNumber = algorithmNumber;
    }
    
    @Override
    public void run()
    {
        try
        {
            File textFile =
                    new File("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Data.txt");
            
            if(textFile.exists())
            {
                try
                {
                    text = Files.readAllLines(textFile.toPath(), Charset.defaultCharset());
                }
                catch(IOException ex) {}
                
                if(text.isEmpty())
                    return;
            }
            
            for(String line : text)
            {
                String[] response = line.split(",");
                
                Process process = new Process(Integer.parseInt(response[0]),
                        Integer.parseInt(response[1]),
                        Integer.parseInt(response[2]),
                        Integer.parseInt(response[3]));
                
                tempProcessList.add(process);
            }
            
            switch(algorithmNumber)
            {
                case 0:
                    firstComeFirstServed();
                    break;
                case 1:
                    npShortestJobFirst();
                    break;
                case 2:
                    pShortestJobFirst();
                    break;
                case 3:
                    pPriority();
                    break;
                case 4:
                    threeRoundRobin();
                    break;
                case 5:
                    fourRoundRobin();
                    break;
                case 6:
                    eightRoundRobin();
                    break;
            }
        }
        catch (IOException ex) 
        {
            Logger.getLogger(SchedulingAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void firstComeFirstServed() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, finishCount;
        int minArrival = processList.get(0).ArrivalTime();
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.ArrivalTime(), p2.ArrivalTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        while(true)
        {
            finishCount = 0;
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(processList.get(k).Finished() == false &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    ganttValue.add(cpuCount);
                    ganttProcess.add(processList.get(k).ProcessNumber());
                
                    cpuCount += processList.get(k).BurstTime() - 1;
                    processList.get(k).finished = true;
                    
                    break;
                }
            }
            
            cpuCount++;
            
            for(int a = 0; a < processList.size(); a++)
                if(processList.get(a).Finished() == true)
                    finishCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        ganttValue.add(cpuCount);
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/1.txt");
        writer.write("Thread 1");
        writer.write("\nFirst Come First Served");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void npShortestJobFirst() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, finishCount;
        int minArrival = processList.get(0).ArrivalTime();
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.BurstTime(), p2.BurstTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        while(true)
        {
            finishCount = 0;
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(processList.get(k).Finished() == false &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    ganttValue.add(cpuCount);
                    ganttProcess.add(processList.get(k).ProcessNumber());
                    
                    cpuCount += processList.get(k).BurstTime() - 1;
                    processList.get(k).finished = true;
                    
                    break;
                }
            }
            
            cpuCount++;
            
            for(int a = 0; a < processList.size(); a++)
                if(processList.get(a).Finished() == true)
                    finishCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        ganttValue.add(cpuCount);
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/2.txt");
        writer.write("Thread 2");
        writer.write("\nNonpreemptive Shortest Job First");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void pShortestJobFirst() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, finishCount = 0;
        int minArrival = processList.get(0).ArrivalTime();
        boolean flag = true;
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.BurstTime(), p2.BurstTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        ganttValue.add(minArrival);
        
        while(true)
        {
            processList.sort((Process p1, Process p2) -> 
                    Integer.compare(p1.BurstTime(), p2.BurstTime()));
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(processList.get(k).BurstTime() > 0 &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    processList.get(k).burstTime--;
                    
                    for(int a = 0; a < processList.size(); a++)
                    {
                        if(processList.get(a).BurstTime() < processList.get(k).BurstTime() &&
                                processList.get(a).BurstTime() != 0 &&
                                processList.get(a).ArrivalTime() <= cpuCount + 1 && 
                                flag)
                        {
                            ganttValue.add(cpuCount + 1);
                            ganttProcess.add(processList.get(k).ProcessNumber());
                            
                            flag = false;
                        }
                    }
                    
                    if(processList.get(k).BurstTime() == 0)
                    {
                        flag = true;
                        finishCount++;
                        
                        ganttValue.add(cpuCount + 1);
                        ganttProcess.add(processList.get(k).ProcessNumber());
                    }
                    
                    break;
                }
            }
            
            cpuCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/3.txt");
        writer.write("Thread 3");
        writer.write("\nPreemptive Shortest Job First");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void pPriority() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, finishCount = 0;
        int minArrival = processList.get(0).ArrivalTime();
        boolean flag = true;
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.PriorityValue(), p2.PriorityValue()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        ganttValue.add(minArrival);
        
        while(true)
        {
            for(int k = 0; k < processList.size(); k++)
            {
                if(processList.get(k).BurstTime() > 0 &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    processList.get(k).burstTime--;
                    
                    for(int a = 0; a < processList.size(); a++)
                    {
                        if(processList.get(a).PriorityValue() < processList.get(k).PriorityValue() &&
                                processList.get(a).BurstTime() != 0 &&
                                processList.get(a).ArrivalTime() <= cpuCount + 1 && 
                                flag)
                        {
                            ganttValue.add(cpuCount + 1);
                            ganttProcess.add(processList.get(k).ProcessNumber());
                            
                            flag = false;
                        }
                    }
                    
                    if(processList.get(k).BurstTime() == 0)
                    {
                        flag = true;
                        finishCount++;
                        
                        ganttValue.add(cpuCount + 1);
                        ganttProcess.add(processList.get(k).ProcessNumber());
                    }
                    
                    break;
                }
            }
            
            cpuCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/4.txt");
        writer.write("Thread 4");
        writer.write("\nPreemptive Priority");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void threeRoundRobin() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, timeQuantum = 3, finishCount;
        int minArrival = processList.get(0).ArrivalTime();
        boolean flag;
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.ArrivalTime(), p2.ArrivalTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        ganttValue.add(minArrival);
        
        while(true)
        {   
            finishCount = 0;
            flag = true;
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(!processList.get(k).Finished() &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    flag = false;
                    processList.get(k).burstTime -= timeQuantum;
                    
                    if(processList.get(k).BurstTime() < 0)
                        cpuCount += timeQuantum + processList.get(k).BurstTime();
                    else
                        cpuCount += timeQuantum;
                    
                    ganttValue.add(cpuCount);
                    ganttProcess.add(processList.get(k).ProcessNumber());
                    
                    if(processList.get(k).BurstTime() <= 0)
                        processList.get(k).finished = true;
                }
            }
            
            if(flag)
                cpuCount++;
            
            for(int a = 0; a < processList.size(); a++)
                if(processList.get(a).Finished() == true)
                    finishCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/5.txt");
        writer.write("Thread 5");
        writer.write("\nRound Robin(TimeQuantum = 3)");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void fourRoundRobin() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, timeQuantum = 4, finishCount;
        int minArrival = processList.get(0).ArrivalTime();
        boolean flag;
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.ArrivalTime(), p2.ArrivalTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        ganttValue.add(minArrival);
        
        while(true)
        {   
            finishCount = 0;
            flag = true;
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(!processList.get(k).Finished() &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    flag = false;
                    processList.get(k).burstTime -= timeQuantum;
                    
                    if(processList.get(k).BurstTime() < 0)
                        cpuCount += timeQuantum + processList.get(k).BurstTime();
                    else
                        cpuCount += timeQuantum;
                    
                    ganttValue.add(cpuCount);
                    ganttProcess.add(processList.get(k).ProcessNumber());
                    
                    if(processList.get(k).BurstTime() <= 0)
                        processList.get(k).finished = true;
                }
            }
            
            if(flag)
                cpuCount++;
            
            for(int a = 0; a < processList.size(); a++)
                if(processList.get(a).Finished() == true)
                    finishCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/6.txt");
        writer.write("Thread 6");
        writer.write("\nRound Robin(TimeQuantum = 4)");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }
    
    public void eightRoundRobin() throws IOException
    {
        long startTime = System.nanoTime();
        
        ArrayList<Process> processList = (ArrayList<Process>)tempProcessList.clone();
        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttValue = new ArrayList<>();
        
        int cpuCount = 0, timeQuantum = 8, finishCount;
        int minArrival = processList.get(0).ArrivalTime();
        boolean flag;
        
        processList.sort((Process p1, Process p2) -> 
                Integer.compare(p1.ArrivalTime(), p2.ArrivalTime()));
        
        for(Process iterator : processList)
            if(iterator.ArrivalTime() < minArrival)
                minArrival = iterator.ArrivalTime();
        
        if(minArrival != 0)
            ganttValue.add(0);
        
        ganttValue.add(minArrival);
        
        while(true)
        {   
            finishCount = 0;
            flag = true;
            
            for(int k = 0; k < processList.size(); k++)
            {
                if(!processList.get(k).Finished() &&
                        processList.get(k).ArrivalTime() <= cpuCount)
                {
                    flag = false;
                    processList.get(k).burstTime -= timeQuantum;
                    
                    if(processList.get(k).BurstTime() < 0)
                        cpuCount += timeQuantum + processList.get(k).BurstTime();
                    else
                        cpuCount += timeQuantum;
                    
                    ganttValue.add(cpuCount);
                    ganttProcess.add(processList.get(k).ProcessNumber());
                    
                    if(processList.get(k).BurstTime() <= 0)
                        processList.get(k).finished = true;
                }
            }
            
            if(flag)
                cpuCount++;
            
            for(int a = 0; a < processList.size(); a++)
                if(processList.get(a).Finished() == true)
                    finishCount++;
            
            if(finishCount == processList.size())
                break;
        }
        
        long endTime = System.nanoTime();
        double algorithmTime = (endTime - startTime) / 1e6;
        
        FileWriter writer = new FileWriter("/home/lykia/NetBeansProjects/MultiThreadApp/src/multithreadapp/Results/7.txt");
        writer.write("Thread 7");
        writer.write("\nRound Robin(TimeQuantum = 8)");
        writer.write("\nGantt Process      ");
        for(Integer iterator : ganttProcess)
            writer.write(iterator + " | ");
        writer.write("\nGantt Time     ");
        for(Integer iterator : ganttValue)
            writer.write(iterator + " ");
        writer.write("\nAlgorithm Time : " + algorithmTime);
        writer.close();
    }

    @Override
    public int compareTo(Process o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}