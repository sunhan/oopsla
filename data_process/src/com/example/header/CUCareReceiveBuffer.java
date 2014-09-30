package com.example.header;

public class CUCareReceiveBuffer {
	private PMD_Packet[] m_cPacketGatewayPulseDataBuffer;
	private CUCareReceiveBuffer	m_pPrev;
	private CUCareReceiveBuffer	m_pNext;
	
	public CUCareReceiveBuffer()
	{
		m_cPacketGatewayPulseDataBuffer = new PMD_Packet[stadafx.MAX_RECEIVE_PACKET_NUMBER];
		m_pNext = null;
		m_pPrev = null;
	}

	public PMD_Packet[] getM_cPacketGatewayPulseDataBuffer() {
		return m_cPacketGatewayPulseDataBuffer;
	}

	public void setM_cPacketGatewayPulseDataBuffer(
			PMD_Packet[] m_cPacketGatewayPulseDataBuffer) {
		this.m_cPacketGatewayPulseDataBuffer = m_cPacketGatewayPulseDataBuffer;
	}

	public CUCareReceiveBuffer getM_pPrev() {
		return m_pPrev;
	}

	public void setM_pPrev(CUCareReceiveBuffer m_pPrev) {
		this.m_pPrev = m_pPrev;
	}

	public CUCareReceiveBuffer getM_pNext() {
		return m_pNext;
	}

	public void setM_pNext(CUCareReceiveBuffer m_pNext) {
		this.m_pNext = m_pNext;
	}
}
