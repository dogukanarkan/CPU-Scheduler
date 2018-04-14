/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadapp;

/**
 *
 * @author lykia
 */
public class Process
{
    public int processNumber;
    public int arrivalTime;
    public int burstTime;
    public int priorityValue;
    public boolean finished;
    
    public Process(int processNumber, 
                   int arrivalTime,
                   int burstTime, 
                   int priorityValue)
    {
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityValue = priorityValue;
        finished = false;
    }
    
    public int ProcessNumber()
    {
        return processNumber;
    }
    
    public int ArrivalTime()
    {
        return arrivalTime;
    }
    
    public int BurstTime()
    {
        return burstTime;
    }
    
    public int PriorityValue()
    {
        return priorityValue;
    }
    
    public boolean Finished()
    {
        return finished;
    }
}
