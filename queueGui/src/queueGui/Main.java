package queueGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

class Node {
	String name;
	// track the customer arrival, departure, and required to service him
	int arrival, departure, serviceTime;

	public Node() {
		this.name = "   ";
		this.arrival = 0;
		this.departure = 0;
	}

	public Node(String name, int arrival, int serviceTime) {
		this.name = name;
		this.arrival = arrival;
		this.departure = 0;
		this.serviceTime = serviceTime;
	}

	public int getAt() {
		return arrival;
	}

	public int getSt() {
		return serviceTime;
	}

	public String getName() {
		return name;
	}

	public int getDt() {
		return departure;
	}
}


class Queue {
	// index of the item at the front of the queue
	int frontIndex;
	Node[] storage;
	int totalCustomers;
	int capacity;
	// track cumulative waiting times for all Customers in this queue
	int noOfCustomersServiced = 0;
	int totalWaitTime = 0;

	public Queue(int capacity) {
		this.capacity = capacity + 1;
		storage = new Node[this.capacity];
		for (int i = 0; i < this.capacity; i++) {
			storage[i] = new Node();
		}
		frontIndex = 0;
	}

	public boolean empty() {
		return totalCustomers == 0;
	}

	public int getFrontDt() {
		return storage[frontIndex].departure;
	}

	public int getNumberOfCust() {
		return totalCustomers;
	}

	public boolean full() {
		return getNumberOfCust() == capacity - 1;
	}

	// enqueue
	public void enq(Node customerTransaction) {
		if (!full()) {
			storage[(frontIndex + totalCustomers) % capacity] = customerTransaction;
			totalCustomers++;
		}
	}

	public void setFrontDt(int dt) {
		storage[frontIndex].departure = dt;
	}

	// dequeue
	public Node deq() {
		if (empty()) {
			return null;
		}
		Node removed = storage[frontIndex];
		storage[frontIndex] = new Node();
		frontIndex = (frontIndex + 1) % capacity;
		totalCustomers--;
		noOfCustomersServiced++;
		// get node's wait time and sum up in total wait time for all serviced customers
		// in the queue
		totalWaitTime += removed.getDt() - removed.getSt() - removed.getAt();
		return removed;
	}

	public int getFrontSt() {
		return storage[frontIndex].getSt();
	}

	public void showCQueueArray(String s) {
		System.out.print(s);
		String result = "";
		for (int i = 0; i < capacity; i++) {
			Node transaction = storage[i];
			result += String.format("%s, %d, %d, %d|", transaction.name, transaction.getAt(), transaction.getSt(),
					transaction.getDt());
		}
		System.out.println(result);
	}

	public String getQueueStatusAsString() {
		String result = "";
		for (int i = 0; i < capacity; i++) {
			Node transaction = storage[i];
			result += String.format("%s, %d, %d, %d|", transaction.name, transaction.getAt(), transaction.getSt(),
					transaction.getDt());
		}
		return result + "\n";
	}

}

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	JLabel labelCustomer = new JLabel("Customer Name");
	JLabel labelArrival = new JLabel("Arrival Time");
	JLabel labelService = new JLabel("Service Time");

	JTextField textFieldCustomerName = new JTextField(10);
	JTextField textFieldArrivalTime = new JTextField(10);
	JTextField textFieldServiceTime = new JTextField(10);

	JButton btnAddCustomer = new JButton("Add Customer");
	JButton btnStartSimulation = new JButton("Start Simulation");

	JPanel panelCustomerControls = new JPanel();

	JPanel panelQueueContents = new JPanel();
	JTextArea txtAreaQueueContents = new JTextArea(15, 40);
	JScrollPane spQueueContents = new JScrollPane(txtAreaQueueContents);

	JPanel panelTrace = new JPanel();
	JTextArea txtAreaTrace = new JTextArea(15, 40);
	JScrollPane spTrace = new JScrollPane(txtAreaTrace);

	JPanel panelAverage = new JPanel();
	JLabel labelAverageWaitTime = new JLabel("Average Wait Time: ");
	JButton btnReset = new JButton("Reset Simulation");

	// dynamic array to store the transactions
	static ArrayList<Node> transactions = new ArrayList<Node>();
	// dynamic array to store the Trace Lines
	static ArrayList<String> traceLines = new ArrayList<>();

	// Queues
	static Queue q1 = new Queue(3);
	static Queue q2 = new Queue(2);

	Main() {
		setTitle("Waiting Line Simulator");
		setSize(550, 700);
		Container c = this.getContentPane();
		this.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

		// add components to individual panels
		panelCustomerControls.setLayout(new GridLayout(2, 4, 2, 2));
		panelCustomerControls.add(labelCustomer);
		panelCustomerControls.add(labelArrival);
		panelCustomerControls.add(labelService);
		panelCustomerControls.add(btnStartSimulation);
		panelCustomerControls.add(textFieldCustomerName);
		panelCustomerControls.add(textFieldArrivalTime);
		panelCustomerControls.add(textFieldServiceTime);
		panelCustomerControls.add(btnAddCustomer);

		// add text areas enclosed in scroll panes
		spQueueContents.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spQueueContents.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelQueueContents.add(spQueueContents);

		spTrace.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spTrace.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelTrace.add(spTrace);

		// add remaining controls
		panelAverage.add(labelAverageWaitTime);
		panelAverage.add(btnReset);

		// add the panels to the frame
		c.add(panelCustomerControls);
		c.add(panelQueueContents);
		c.add(panelTrace);
		c.add(panelAverage);

		btnAddCustomer.addActionListener(this);
		btnStartSimulation.addActionListener(this);
		btnReset.addActionListener(this);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton gButton = (JButton) e.getSource();
		if (gButton == btnAddCustomer) {
			addCustomerToArray();
		} else if (gButton == btnStartSimulation) {
			// runSimulation();
			if (transactions.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please add Customers first!");
			} else {
				runSimulation();
			}
		} else if (gButton == btnReset) {
			clearInputFields();
			txtAreaQueueContents.setText("");
			txtAreaTrace.setText("");
			labelAverageWaitTime.setText("Average Wait Time: ");
			transactions.clear();
		}
	}

	private void addCustomerToArray() {
		if (textFieldArrivalTime.getText().isEmpty() || textFieldServiceTime.getText().isEmpty()
				|| textFieldCustomerName.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Please provide the values for Customer Name, Arrival Time, and Service Time!");
			return;
		}
		String customerName = textFieldCustomerName.getText();
		try {
			int arrivalTime = Integer.parseInt(textFieldArrivalTime.getText());
			int serviceTime = Integer.parseInt(textFieldServiceTime.getText());
			Node customer = new Node(customerName, arrivalTime, serviceTime);
			// ignore duplicate customers
			if (!checkForDuplicates(customer)) {
				transactions.add(customer);
			} else {
				JOptionPane.showMessageDialog(this, "Duplicate Transaction!, Please Try Again.");
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please provide numerical values for the Arrival and Service time!");
		}
		clearInputFields();
	}

	// Check if Transaction already added to the transactions list
	private boolean checkForDuplicates(Node customer) {
		for (Node c : transactions) {
			if (customer.name.equals(c.name) && customer.arrival == c.arrival
					&& customer.serviceTime == c.serviceTime) {
				return true;
			}
		}
		return false;
	}

	// Clear all text boxes used for input
	private void clearInputFields() {
		textFieldCustomerName.setText("");
		textFieldServiceTime.setText("");
		textFieldArrivalTime.setText("");
	}

	static int determineEventType(int i) {
		int eType; // 1:Arrival 2: Departure
		if (q1.empty() && q2.empty()) {
			eType = 1;
		} else {
			if (!q1.empty() && q2.empty()) {
				if (transactions.get(i).getAt() < q1.getFrontDt())
					eType = 1;
				else
					eType = 21;
			} else if (!q2.empty() && q1.empty()) {
				if (transactions.get(i).getAt() < q2.getFrontDt()) {
					eType = 1;
				} else {
					eType = 22;
				}
			} else if (transactions.get(i).getAt() < q1.getFrontDt() && transactions.get(i).getAt() < q2.getFrontDt()) {
				eType = 1;
			} else {
				if (q1.getFrontDt() <= q2.getFrontDt()) {
					eType = 21;
				} else {
					eType = 22;
				}
			}
		}
		return eType;
	}

	void runSimulation() {

		// addCustomers();
		String queueStatus = "";

		transactions.add(new Node("Done", 10000, 0));

		Node node = null, currentTransaction;
		int eventType;
		int i = 0, numOfTransactions = transactions.size();

		while (i < numOfTransactions) {
			eventType = determineEventType(i);
			currentTransaction = transactions.get(i);
			if (eventType == 1) {// it is an arrival event
				if (q2.getNumberOfCust() < q1.getNumberOfCust()) {
					if (!q2.full()) {
						q2.enq(currentTransaction);
						if (q2.getNumberOfCust() == 1) {
							q2.setFrontDt(currentTransaction.getAt() + currentTransaction.getSt());
						}
					} else {
						System.out.println(currentTransaction.getName() + " rejected");
						queueStatus += currentTransaction.getName() + " rejected\n";
					}
				} else {
					if (!q1.full()) {
						q1.enq(currentTransaction);
						if (q1.getNumberOfCust() == 1) {
							q1.setFrontDt(currentTransaction.getAt() + currentTransaction.getSt());
						}
					} else {
						System.out.println(currentTransaction.getName() + " rejected");
						queueStatus += currentTransaction.getName() + " rejected\n";
					}
				}
				i++;
			} else {// it is a departure event
				if (eventType == 21) {
					node = q1.deq();
					System.out.println(node.getName() + " Left @ " + node.getDt());
					queueStatus += node.getName() + " Left @ " + node.getDt() + "\n";

					if (!q1.empty()) {
						q1.setFrontDt(node.getDt() + q1.getFrontSt());
					}
				} else {
					node = q2.deq();
					System.out.println(node.getName() + " Left @ " + node.getDt());
					queueStatus += node.getName() + " Left @ " + node.getDt() + "\n";

					if (!q2.empty()) {
						q2.setFrontDt(node.getDt() + q2.getFrontSt());
					}
				}
			}
			if (i < numOfTransactions) {
				q1.showCQueueArray("Q1: ");
				q2.showCQueueArray("Q2: ");
				queueStatus += "Q1: " + q1.getQueueStatusAsString();
				queueStatus += "Q2: " + q2.getQueueStatusAsString();
				queueStatus += "\n";
				System.out.println();
			} else {
				q1.deq();
			}
			Node traceNode = null;
			if (eventType == 1) {
				traceNode = currentTransaction;
			} else {
				traceNode = node;
			}
			traceLines.add(i + "\t" + traceNode.arrival + "\t" + eventType + "\t" + traceNode.getName() + "\t"
					+ traceNode.getSt() + "\t" + traceNode.getDt() + "\t"
					+ (traceNode.getDt() - traceNode.getSt() - traceNode.getAt()) + "\n");

		} // end of while
			// Calculate and Display Queue Average Wait Times
		System.out.println("Average Wait Time of Queue 1: " + (float) q1.totalWaitTime / q1.noOfCustomersServiced);
		System.out.println("Average Wait Time of Queue 2: " + (float) q2.totalWaitTime / q2.noOfCustomersServiced);
		System.out.println("Average Wait Time of Both Queues: " + (float) (q1.totalWaitTime + q2.totalWaitTime)
				/ (q1.noOfCustomersServiced + q2.noOfCustomersServiced));
		System.out.println();

		float averageWaitTime = (float) (q1.totalWaitTime + q2.totalWaitTime)
				/ (q1.noOfCustomersServiced + q2.noOfCustomersServiced);
		txtAreaQueueContents.setText(queueStatus);
		String traceLineContents = "Clock\tAT\tEvent Type\tName\tST\tDT\tWT\n";
		for (String trace : traceLines) {
			traceLineContents += trace;
		}
		txtAreaTrace.setText(traceLineContents);
		labelAverageWaitTime.setText(labelAverageWaitTime.getText() + averageWaitTime);
	}

	private static void addCustomers() {

		transactions.add(new Node("Adams", 4, 8));
		transactions.add(new Node("Kim", 5, 6));
		transactions.add(new Node("Joe", 6, 2));
		transactions.add(new Node("Sue", 8, 4));
		transactions.add(new Node("Jack", 9, 3));
		transactions.add(new Node("John", 10, 2));
		transactions.add(new Node("Max", 12, 6));
		transactions.add(new Node("Jill", 13, 5));
		transactions.add(new Node("Done", 10000, 0));

	}
}