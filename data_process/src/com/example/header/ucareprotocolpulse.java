package com.example.header;

//게이트웨이로부터 수신되는 데이터 패킷 클래스
class PMD_Packet {
	long m_nTimeCount; // BigInteger 크기에 문제가 생길 경우.
	int m_bReceiverMode;
	int m_bBatteryStatus;
	short m_nCardioChannel1;
	short m_nCardioChannel2;
	short m_nAxisX;
	short m_nAxisY;
	short m_nAxisZ;
	short m_nTemperature;
	
	public long getM_nTimeCount() {
		return m_nTimeCount;
	}
	public void setM_nTimeCount(long m_nTimeCount) {
		this.m_nTimeCount = m_nTimeCount;
	}
	public int getM_bReceiverMode() {
		return m_bReceiverMode;
	}
	public void setM_bReceiverMode(int m_bReceiverMode) {
		this.m_bReceiverMode = m_bReceiverMode;
	}
	public int getM_bBatteryStatus() {
		return m_bBatteryStatus;
	}
	public void setM_bBatteryStatus(int m_bBatteryStatus) {
		this.m_bBatteryStatus = m_bBatteryStatus;
	}
	public short getM_nCardioChannel1() {
		return m_nCardioChannel1;
	}
	public void setM_nCardioChannel1(short m_nCardioChannel1) {
		this.m_nCardioChannel1 = m_nCardioChannel1;
	}
	public short getM_nCardioChannel2() {
		return m_nCardioChannel2;
	}
	public void setM_nCardioChannel2(short m_nCardioChannel2) {
		this.m_nCardioChannel2 = m_nCardioChannel2;
	}
	public short getM_nAxisX() {
		return m_nAxisX;
	}
	public void setM_nAxisX(short m_nAxisX) {
		this.m_nAxisX = m_nAxisX;
	}
	public short getM_nAxisY() {
		return m_nAxisY;
	}
	public void setM_nAxisY(short m_nAxisY) {
		this.m_nAxisY = m_nAxisY;
	}
	public short getM_nAxisZ() {
		return m_nAxisZ;
	}
	public void setM_nAxisZ(short m_nAxisZ) {
		this.m_nAxisZ = m_nAxisZ;
	}
	public short getM_nTemperature() {
		return m_nTemperature;
	}
	public void setM_nTemperature(short m_nTemperature) {
		this.m_nTemperature = m_nTemperature;
	}

};

//게이트웨이에 데이터를 요청하는 데이터 패킷 클래스
class PMD_PacketRequest {
	int m_bCommand;
	int m_nReceiverID;
	char[] m_strSenderID;
	int m_nSenderChannel;
	int m_bReserved;
	
	
	public PMD_PacketRequest(int m_bCommand, int m_nReceiverID, char[] m_strSenderID, int m_nSenderChannel, int m_bReserved)
	{
		this.m_bCommand = m_bCommand;
		this.m_nReceiverID = m_nReceiverID;
		this.m_nSenderChannel = m_nSenderChannel;
		this.m_bReserved = m_bReserved;
		//this.m_strSenderID = m_strSenderID;
		
		this.m_strSenderID = new char[m_strSenderID.length]; 
		System.arraycopy(m_strSenderID, 0, this.m_strSenderID, 0, this.m_strSenderID.length); //배열 복사.
	}

	public int getM_bCommand() {
		return m_bCommand;
	}

	public void setM_bCommand(int m_bCommand) {
		this.m_bCommand = m_bCommand;
	}
	
	public int getM_nReceiverID() {
		return m_nReceiverID;
	}

	public void setM_nReceiverID(int m_nReceiverID) {
		this.m_nReceiverID = m_nReceiverID;
	}

	public int getM_nSenderChannel() {
		return m_nSenderChannel;
	}

	public void setM_nSenderChannel(int m_nSenderChannel) {
		this.m_nSenderChannel = m_nSenderChannel;
	}

	public int getM_bReserved() {
		return m_bReserved;
	}

	public void setM_bReserved(int m_bReserved) {
		this.m_bReserved = m_bReserved;
	}
};

//데이터 요청에 대한 송수신기 상태를 응답하는 데이터 패킷 클래스
class PMD_PacketResponse
{
	int	m_bCommand;
	int	m_nReceiverID;
	char[]	m_strSenderID;
	int	m_nSenderChannel;
	int	m_bStatus;
	
	public PMD_PacketResponse()
	{
		m_strSenderID = new char[8];
	}

	public int getM_bCommand() {
		return m_bCommand;
	}

	public void setM_bCommand(int m_bCommand) {
		this.m_bCommand = m_bCommand;
	}

	public int getM_nReceiverID() {
		return m_nReceiverID;
	}

	public void setM_nReceiverID(int m_nReceiverID) {
		this.m_nReceiverID = m_nReceiverID;
	}

	public int getM_nSenderChannel() {
		return m_nSenderChannel;
	}

	public void setM_nSenderChannel(int m_nSenderChannel) {
		this.m_nSenderChannel = m_nSenderChannel;
	}

	public int getM_bStatus() {
		return m_bStatus;
	}

	public void setM_bStatus(int m_bStatus) {
		this.m_bStatus = m_bStatus;
	}
};