package me.nemo_64.a2.so.event;

public interface ProcessListener {

    void processAdded(ProcessAddedEvent e);

    void processRemoved(ProcessRemovedEvent e);

}
