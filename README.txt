Elevator Simulator Project - Iteration 4

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
Iteration 3: Multiple Cars and System Distribution (completed)
Iteration 4: Adding Error detection and correction (current version)
 - Adding Error detection and correction.
Iteration 5: Measuring the Scheduler and predicting the performance

Design:
For better understanding the design of the project please navigate to the "./diagrams" folder.
 - The UML class diagram for this iteration is located in "./diagrams/class_diagrams" labelled "class_diagram_iter4.png".
 - The sequence diagrams is located in "./diagram/sequence_diagrams" labelled: "iter4_sequence_diagram.jpg".
 - The state diagrams are located in "./diagrams/state_diagrams" as follows:
      - "elevator_state_machine.jpg"
      - "scheduler_state_machine.jpg"

Contents:
 - Package ElevatorSimulator
      - ErrorGenerator.java : The class in charge of generating an elevator for the elevator.
      - Logger.java : Format the message to print to the console.
      - Serializer.java : serialize or deserialize a given message.
      - Simulator.java : The class containing static variables.
      - Timer.java : The timer class.
 - Package ElevatorSimulator.Elevator
      - Elevator.java : The elevator subsystem, receives and replies to messages.
      - ElevatorController.java : Responsible for controlling the multiple elevators.
      - ElevatorInfo.java : The class in charge of holding the elevator status used for transferring to the scheduler.
      - ElevatorState.java : Enum for The possible states of the elevator.
      - ElevatorTrip.java : Class defining an elevator trip in a given flow.
 - Package ElevatorSimulator.Floor
      - Floor.java : The floor subsystem.
 - Package ElevatorSimulator.Messages
      - ACKMessage.java : Acknowledgment message.
      - ArrivedElevatorMessage.java : Message to indicate the elevator has arrived.
      - DirectionType.java : Enum for the possible directions of the elevator.
      - DoorOpenedMessage.java : Message to indicate that the doors have opened.
      - ElevatorStuckMessage.java : Depicts when an elevator is stuck.
      - EmptyMessage.java : The empty message returned when there are no requests to send to the subsystem.
      - ErrorType.java : Error types for the Elevator.
      - GetUpdateMessage.java : The message sent by the subsystem to get an update.
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
      - elevator_input7.csv : Different test case commands.
 - Package ElevatorSimulator.Scheduler
      - Scheduler.java : The scheduler subsystem that removes a message from the elevator and sends it to the floor and removes a message from the floor and sends it to the elevator.
      - SchedulerState.java : Enum for the possible scheduler states.
 - Package ElevatorSimulatorTest
	- MockServerRPC.java : A mock version of the ServerRPC class with a work queue in the background which holds the latest request.
 - Package ElevatorSimulatorTest.ElevatorTest
      - ElevatorIntegrationTest.java : Tests the general flow for the elevator.
      - ElevatorUnitTest.java : Unit Tests for the elevator.
 - Package ElevatorSimulatorTest.FloorTest
      - FloorTest.java : The unit tests for the floor subsystem.
 - Package ElevatorSimulatorTest.SchedulerTest
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

Iteration 4:
 - Andre Hazim:
	- Elevator error detection
 - Bardia Parmoun:
	- Scheduler updates, Elevator test
 - Guy Morgenshtern:
	- Elevator error detection
 - Kyra Lothrop:
	- Floor updates, Scheduler test
 - Sarah Chow:
      - Floor updates, Floor test


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
- ElevatorIntegrationTest: Tests the general flow for the elevator.
- ElevatorUnitTest: Unit Tests for the elevator.
- FloorTest: testing the floor subsystem alone.
- SchedulerTest: testing the scheduler subsystem alone.

Navigate to any of these classes and run them as a JUnit test to confirm that the system is
working as expected. 
