# Elevator Simulator Project

<p align="center">
<img src="demo/demo.png" />
</p>

## Authors (Group L1G3)
[@Andre Hazim](https://github.com/andre-Hazim) \
[@Bardia Parmoun](https://github.com/bardia-p) \
[@Guy Morgenshtern](https://github.com/guymorgenshtern) \
[@Kyra Lothrop](https://github.com/kyralothrop) \
[@Sarah Chow](https://github.com/sarahchoww)

## Description
The purpose of this project is to simulate the behaviour of an elevator system in real time. 
The Elevator Simulator is composed of the Scheduler, Elevator, and Floor subsystems, that are constantly interacting with each other.

## Deliverables
- **Iteration 0:** Measure a Real Elevator **(completed)**
- **Iteration 1:** Establish Connections between the three subsystems **(completed)**
- **Iteration 2:** Adding the scheduler and Elevator Subsystem **(completed)**
- **Iteration 3:** Multiple Cars and System Distribution **(completed)**
- **Iteration 4:** Adding Error detection and correction **(completed)**
- **Iteration 5:** Measuring the Scheduler and predicting the performance **(current version)**

## Design
- All the design diagrams for the project can be found at [diagrams](https://github.com/bardia-p/Elevator-Simulator/tree/main/diagrams). This folder contains:
     - The latest [UML class diagram](https://github.com/bardia-p/Elevator-Simulator/blob/main/diagrams/class_diagrams/class_diagram_iter5.jpg) for the system.
     - The [sequence diagrams](https://github.com/bardia-p/Elevator-Simulator/tree/main/diagrams/sequence_diagrams) showing the interactions between the components.
     - The [state machines](https://github.com/bardia-p/Elevator-Simulator/tree/main/diagrams/state_diagrams/Iteration4) for the elevator and scheduler subsystems.
     - The [timing diagrams](https://github.com/bardia-p/Elevator-Simulator/tree/main/diagrams/timing_diagrams) showing the behaviour of the system during error cases.

- You can also review the [design report](https://github.com/bardia-p/Elevator-Simulator/blob/main/documents/Final%20Report.pdf) describing various aspects of the project in detail. 
- Here is also the [elevator measurements](https://github.com/bardia-p/Elevator-Simulator/blob/main/documents/L1G3_milestone_0.pdf) collected in the first iteration of the project.
- And don't forget to check out [project demo!](https://www.youtube.com/watch?v=WC1NvLRQY9o)

## Setup instructions
### Setting up the project
1. Unzip the submission file.
2. Navigate the eclipse IDE.
3. Navigate the File menu.
4. Open the `Open Project from File System..` option.
5. Select the root folder for the project `ElevatorSimulator`.
6. Click the `Finish button`.
7. Select the project folder. 

### Running the project on one system using localhost
Once the project folder is open navigate to the `./src/ElevatorSimulator/` and run these files in the following order.
   - `Scheduler/Scheduler.java`
   - `Elevator/ElevatorController.java`
   - `UI/UIGenerator.java`
   - `Floor/Floor.java`

### Running the system on multiple devices:
1. Run the Scheduler.java on your machine to find your IP address.
2. On every other machine, update the PUBLIC_IP parameter of `Message/ServerRPC.java` file with the IP address that you obtained.
3. Update the main function for `UIGenerator`, `Floor`, and `ElevatorController` by changing their connection type to `ConnectionType` to `REMOTE`.
4. Start the subsystems on each machine with the same order as before.


## Test Instructions
The `ElevatorSimulatorTest` package has been dedicated to testing. The testing framework that was used in this project is JUnit 5.

For each subsystem there is a dedicated test class to test them separately.

```
- ElevatorIntegrationTest: Tests the general flow for the elevator.
- ElevatorUnitTest: Unit Tests for the elevator.
- FloorTest: testing the floor subsystem alone.
- SchedulerTest: testing the scheduler subsystem alone.
- PerformanceUnitTest: measuring specific elevator events.
- PerformanceIntegrationTest: measuring the system as a whole.
```

Navigate to any of these classes and run them as a JUnit test to confirm that the system is working as expected. 

## File Breakdown
```
. src
├── Package ElevatorSimulator
|    ├── ErrorGenerator.java : The class in charge of generating an elevator for the elevator.
|    ├── Logger.java : Format the message to print to the console.
|    ├── Serializer.java : serialize or deserialize a given message.
|    ├── Simulator.java : The class containing static variables.
|    ├── Timer.java : The timer class.
|    ├── Package ElevatorSimulator.Elevator
|    |    ├── Elevator.java : The elevator subsystem, receives and replies to messages.
|    |    ├── ElevatorController.java : Responsible for controlling the multiple elevators.
|    |    ├── ElevatorInfo.java : The class in charge of holding the elevator status used for transferring to the scheduler.
|    |    ├── ElevatorState.java : Enum for The possible states of the elevator.
|    |    ├── ElevatorTrip.java : Class defining an elevator trip in a given flow.
|    ├── Package ElevatorSimulator.Floor
|    |    ├── Floor.java : The floor subsystem.
|    ├── Package ElevatorSimulator.Messages
|    |    ├── ACKMessage.java : Acknowledgment message.
|    |    ├── ArrivedElevatorMessage.java : Message to indicate the elevator has arrived.
|    |    ├── DirectionType.java : Enum for the possible directions of the elevator.
|    |    ├── DoorInterruptMessage : Transient error message class.
|    |    ├── DoorOpenedMessage.java : Message to indicate that the doors have opened.
|    |    ├── ElevatorStuckMessage.java : Depicts when an elevator is stuck.
|    |    ├── EmptyMessage.java : The empty message returned when there are no requests to send to the subsystem.
|    |    ├── ErrorType.java : Error types for the Elevator.
|    |    ├── GetUpdateMessage.java : The message sent by the subsystem to get an update.
|    |    ├── KillMessage.java : Message used to kill the system.
|    |    ├── Message.java : The default message class for holding the information that is passed to the buffers.
|    |    ├── MessageType.java : Enum for the possible message types passed in the buffer. 
|    |    ├── ReadyMessage.java : Ready message.
|    |    ├── RequestElevatorMessage.java : Message for requesting the elevator.
|    |    ├── SenderType.java : Enum for the subsystems that can send messages.
|    |    ├── StartMessage.java : Start message.
|    |    ├── StopType.java : Enum for the possible reasons that the doors have opened.
|    |    ├── UpdateElevatorInfoMessage.java : Message to update the elevator information.
|    ├── Package ElevatorSimulator.Messaging
|    |    ├── ClientRPC.java : Client RPC to send and receive packets.
|    |    ├── ConnectionType.java : The connection type for the system.
|    |    ├── MessageQueue.java : File representing the MessageQueue class.
|    |    ├── ServerRPC.java : Server RPC to send and receive packets.
|    ├── Package ElevatorSimulator.Scheduler
|    |    ├──  Scheduler.java : The scheduler subsystem in charge of managing the requests and assigning tasks to the elevator.s
|    |    ├── SchedulerState.java : Enum for the possible scheduler states.
├── Package ElevatorSimulatorTest
|    ├── MockServerRPC.java : A mock version of the ServerRPC class with a work queue in the background which holds the latest request.
|    ├── Package ElevatorSimulatorTest.ElevatorTest
|    |    ├── ElevatorIntegrationTest.java : Tests the general flow for the elevator.
|    |    ├── ElevatorUnitTest.java : Unit Tests for the elevator.
|    ├── Package ElevatorSimulatorTest.FloorTest
|    |    ├── FloorTest.java : The unit tests for the floor subsystem.
|    |    ├── FloorParserTest.java : The unit tests to validate the parsing aspect of the floor.
|    ├── Package ElevatorSimulatorTest.SchedulerTest
|    |    ├── SchedulerTest.java : The unit tests for the scheduler subsystem.
|    ├── Package ElevatorSimulatorTest.PerformanceTest
|    |    ├── PerformanceIntegrationTest.java : Measuring the performance of different parts of the system.
|    |    ├── PerformanceUnitTest.java : Measuring specific elevator events.
```

## Individual Contributions
### Iteration 1
 - **Andre Hazim**
	- ElevatorSimulator: Elevator.java, Scheduler.java, ArrivedElevatorMessage.java
	- ElevatorSimulatorTest: SimulatorTest.java
 - **Bardia Parmoun**
	- ElevatorSimulator: Buffer.java, Scheduler.java, Message.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java, SimulatorTest.java
 - **Guy Morgenshtern**
	- ElevatorSimulator: Floor.java, Simulator.java, Message.java, KillMessage.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - **Kyra Lothrop**
	- ElevatorSimulator: Buffer.java, Elevator.java, ArrivedElevatorMessage.java, KillMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - **Sarah Chow**
	- ElevatorSimulator: Floor.java, Simulator.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java

### Iteration 2
 - **Andre Hazim**
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - **Bardia Parmoun**
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java
 - **Guy Morgenshtern**
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - **Kyra Lothrop**
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: FloorTest.java, SchedulerTest.java
 - **Sarah Chow**
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, SchedulerTest.java

### Iteration 3
 - **Andre Hazim**
	- Elevator updates and UDP implementation.
 - **Bardia Parmoun**
	- Scheduler updates and UDP implementation.
 - **Guy Morgenshtern**
	- Elevator updates and UDP implementation.
 - **Kyra Lothrop**
	- JUnit testing and Floor updates.
 - **Sarah Chow**
    - JUnit testing and Scheduler updates.

### Iteration 4
 - **Andre Hazim**
	- Updating the elevator to handle door interrupts and elevator stuck.
	- Added regression and unit tests for the tests.
 - **Bardia Parmoun**
	- Updated the scheduler to handle elevator faults.
	- Added regression and unit tests for the elevator.
 - **Guy Morgenshtern**
 	- Updating the elevator to handle door interrupts and elevator stuck.
 	- Added regression and unit tests for the tests.
 - **Kyra Lothrop**
	- Updated the floor to handle sending files with error.
	- Added unit tests for the scheduler.
 - **Sarah Chow**
 	- Updated the floor to handle sending files with error.

### Iteration 5
 - **Andre Hazim**
      - Developed the UI for the system.
      - Contributed to the project report.
 - **Bardia Parmoun**
	- Added performance tests to measure various aspects of the system.
      - Contributed to the project report.
 - **Guy Morgenshtern**
      - Developed the UI for the system.
      - Contributed to the project report.
 - **Kyra Lothrop**
      - Added performance tests to measure various aspects of the system.
      - Contributed to the project report.
 - **Sarah Chow**
      - Developed the UI for the system.
      - Contributed to the project report and prepared the demo video.