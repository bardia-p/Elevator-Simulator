Elevator Simulator Project - Iteration 3

Authors (Group L1G3)
 - Andre Hazim - 101141843
 - Bardia Parmoun - 101143006
 - Guy Morgenshtern - 101151430
 - Kyra Lothrop - 101145872
 - Sarah Chow - 101143033

Description:
The purpose of this project is to simulate the behaviour of an elevator system in real time. 
The Elevator Simulator is composed of the Scheduler, Elevator, and Floor subsystems, that are constantly interacting with each other.

Deliverables:
Iteration 0: Measure a Real Elevator (completed)
Iteration 1: Establish Connections between the three subsystems (completed)
Iteration 2: Adding the scheduler and Elevator Subsystem (completed)
Iteration 3: Multiple Cars and System Distribution (current version)
 - Divide the program so that is can run on three seperate computers and communicate using UDP.
Iteration 4: Adding Error detection and correction
Iteration 5: Measuring the Scheduler and predicting the performance

Design:
For better understanding the design of the project please navigate to the "./diagrams" folder.
 - The UML class diagram for this iteration is located in "./diagrams/class_diagrams" labelled "class_diagram_iter3.png".
 - The sequence diagrams are located in "./diagrams/sequence_diagrams/Iteration2" as follows:
	- "elevator_to_floor.png"
	- "floor_to_elevator.png"
 - The state diagrams are located in "./diagrams/state_diagrams" as follows:
      - "elevator_state_machine.jpg"
      - "scheduler_state_machine.jpg"

Contents:
 - Package ElevatorSimulator
      - Logger.java : Format the message to print to the console.
      - Serializer.java : serialize or deserialize a given message.
      - Simulator.java : The class containing static variables.
      - Timer.java : The timer class.
 - Package ElevatorSimulator.Elevator
      - Elevator.java : The elevator subsystem, receives and replies to messages.
      - ElevatorController.java : Responsible for controlling the multiple elevators.
      - ElevatorState.java : Enum for The possible states of the elevator.
 - Package ElevatorSimulator.Floor
      - Floor.java : The floor subsystem.
 - Package ElevatorSimulator.Messages
      - ACKMessage.java : Acknowledgment message.
      - ArrivedElevatorMessage.java : Message to indicate the elevator has arrived.
      - DirectionType.java : Enum for the possible directions of the elevator.
      - DoorOpenedMessage.java : Message to indicate that the doors have opened.
      - KillMessage.java : Message used to kill the system.
      - Message.java : The default message class for holding the information that is passed to the buffers.
      - MessageType.java : Enum for the possible message types passed in the buffer. 
      - ReadyMessage.java : Ready message.
      - RequestElevatorMessage.java : Message for requesting the elevator.
      - SenderType.java : Enum for the subsystems that can send messages.
      - StartMessage.java : Start message.
      - StopType.java : Enum for the possible reasons that the doors have opened.
      - UpdateElevatorInfoMessage.java : Message to update the elevator information.
 - Package ElevatorSimulator.Messaging
      - ClientRPC.java : Client RPC to send and receive packets.
      - MessageQueue.java : File representing the MessageQueue class.
      - ServerRPC.java : Server RPC to send and receive packets.
 - Package ElevatorSimulator.resources
      - elevator_input.csv : csv file containing commands.
      - elevator_input2.csv : Different test case commands.
      - elevator_input3.csv : Different test case commands.
      - elevator_input4.csv : Different test case commands.
      - elevator_input5.csv : Different test case commands.
      - elevator_input6.csv : Different test case commands.
 - Package ElevatorSimulator.Scheduler
      - Scheduler.java : The scheduler subsystem that removes a message from the elevator and sends it to the floor and removes a message from the floor and sends it to the elevator.
      - SchedulerState.java : Enum for the possible scheduler states.
 - Package ElevatorSimulatorTest
      - ElevatorTest.java : The unit tests for the elevator subsystem.
      - FloorTest.java : The unit tests for the floor subsystem.
      - SchedulerTest.java : The unit tests for the scheduler subsystem.
 - Package ElevatorSimulator.TestFiles
      - elevator_test-1.csv : csv file with commands for testing purposes.


* Note: if you notice any ".DS_STORE" files, they are automatically created and are related to the internal file structure of macOS. 

Individual Contributions: 
Iteration 1:
 - Andre Hazim: Elevator and Scheduler subsystem + testing
	- ElevatorSimulator: Elevator.java, Scheduler.java, ArrivedElevatorMessage.java
	- ElevatorSimulatorTest: SimulatorTest.java
 - Bardia Parmoun: Scheduler subsystem + testing
	- ElevatorSimulator: Buffer.java, Scheduler.java, Message.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java, SimulatorTest.java
 - Guy Morgenshtern: Floor subsystem + testing
	- ElevatorSimulator: Floor.java, Simulator.java, Message.java, KillMessage.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - Kyra Lothrop: Elevator subsystem + testing
	- ElevatorSimulator: Buffer.java, Elevator.java, ArrivedElevatorMessage.java, KillMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - Sarah Chow: Floor subsystem + testing
	- ElevatorSimulator: Floor.java, Simulator.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java

Iteration 2:
 - Andre Hazim:
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - Bardia Parmoun:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java
 - Guy Morgenshtern:
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - Kyra Lothrop:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: FloorTest.java, SchedulerTest.java
 - Sarah Chow:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, SchedulerTest.java

Iteration 3:
 - Andre Hazim:
	- Elevator updates and UDP implementation.
 - Bardia Parmoun:
	- Scheduler updates and UDP implementation.
 - Guy Morgenshtern:
	- Elevator updates and UDP implementation.
 - Kyra Lothrop:
	- JUnit testing and Floor updates.
 - Sarah Chow:
    - JUnit testing and Scheduler updates.


Setup instructions:
1. Unzip the submission file.
2. Navigate the eclipse IDE.
3. Navigate the File menu.
4. Open the "Open Project from File System.." option.
5. Select the root folder for the project "ElevatorSimulator".
6. Click the "Finish button".
7. Select the project folder. 
8. Once the project folder is open navigate to the "./src/ElevatorSimulator/" and run these files in the following order.
   - Scheduler.java
   - ElevatorController.java
   - Floor.java

Test Instructions:
The ElevatorSimulator.Test package has been dedicated to testing. The testing framework that was used in this project is JUnit 5.

For each subsystem there is a dedicated test class to test them separately.
- FloorTest: testing the floor subsystem alone.
- ElevatorTest: testing the elevator subsystem alone.
- SchedulerTest: testing the scheduler subsystem alone.

Navigate to any of these classes and run them as a JUnit test to confirm that the system is
working as expected. 
