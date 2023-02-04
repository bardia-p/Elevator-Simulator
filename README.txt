Description
The Elevator Simulator is composed of a Scheduler, Elevators and Floors, with the goal of creating an elevator control system and simulator.

Deliverables
Iteration 0: Measure a Real Elevator (completed)
Iteration 1: Establish Connections between the three subsystems (current version)
 - Development for the three threads of Floor, Elevator and Scheduler.
Iteration 2: Adding the scheduler and Elevator Subsystem
Iteration 3: Multiple Cars and System Distribution
Iteration 4: Adding Error detection and correction
Iteration 5: Measuring the Scheduler and predicting the performance

Contents
- Package ElevatorSimulator
	- Buffer.java : An implementation of a simple blocking queue.
	- Elevator.java : The elevator.
	- Floor.java : Building floors.
	- Scheduler.java : The scheduler subsystem.
	- Simulator.java : Starts the subsystems.
- Package ElevatorSimulator.Messages
	- ArrivedElevatorMessage.java : Message for elevator arriving.
	- DirectionType.java : Enum for the direction of travel of the elevator.
	- KillMessage.java : Specific message for killing the system.
	- Message.java : The default messages that are passed in the buffers.
	- MessageType.java : Enum for message types.
	- RequestElevatorMessage.java : Message for elevator requests.
	- SenderType.java : Enum for types of senders.
- Package ElevatorSimulator.resources
	- elevator_input.csv : csv file containing commands that the simulator takes in.
- Package ElevatorSimulatorTest
	- BufferTest.java : Tests for the buffer class.
	- ElevatorTest.java : Tests for the elevator subsystem.
	- FloorTest.java : Tests for the floor subsystem.
	- MockSheduler.java : A mock version of the Scheduler class for testing purposes.
	- SchedulerTest.java : Tests for the scheduler system.
	 - SimulatorTest.java : Tests for the simulator.
- Package ElevatorSimulatorTest.TestFiles
	- elevator_test-1.csv : csv file with commands for testing purposes.


Setup instructions:
 - Open Eclipse Java IDE
 - Import Elevator-Simulator.zip
 - Run the main method in Simulator.java

Authors (Group L1G3)
 - Andre Hazim - 101141843
	- Iteration 1: Elevator subsystem and test cases simulator test.
 - Bardia Parmoun - 101143006
	- Iteration 1: Scheduler subsystem and test cases elevator test, floor test, mock scheduler, scheduler test and simulator test.
 - Guy Morgenshtern - 101151430
	- Iteration 1: Floor subsystem and test cases buffer test.
 - Kyra Lothrop - 101145872
	- Iteration 1: Elevator subsystem and test cases buffer test.
 - Sarah Chow - 101143033
	- Iteration 1: Floor subsystem and test cases elevator test, floor test, mock scheduler, scheduler test.
