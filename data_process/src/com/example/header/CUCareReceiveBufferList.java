package com.example.header;

public class CUCareReceiveBufferList{
	protected CUCareReceiveBuffer m_pHead;
	protected CUCareReceiveBuffer m_pTail;
	protected long m_nCount;
	
	public CUCareReceiveBufferList(){
		m_pHead = null;
		m_pTail = null;
		m_nCount = 0;
	}
	
	//머리에 추가
	public CUCareReceiveBuffer AddHead(CUCareReceiveBuffer pHead){
		if	(	m_pHead != null	)	{
			pHead.setM_pNext(pHead);
			m_pHead.setM_pPrev(pHead);
			pHead.setM_pPrev(null);
			m_pHead	= pHead;
		}
		else	{
			m_pHead	=	pHead;
			m_pHead.setM_pNext(null);
			m_pHead.setM_pPrev(null);			
			m_pTail	= m_pHead;
		}
		m_nCount++;
		
		if	(	m_nCount	>	stadafx.MAX_RECEIVE_BUFFER_SIZE	)	return	RemoveTail	();
		
		return	null;
	}
	
	//꼬리에 추가
	public CUCareReceiveBuffer AddTail(CUCareReceiveBuffer pTail){
		
		if(m_pTail != null){
			m_pTail.setM_pNext(pTail);
			pTail.setM_pPrev(m_pTail);
			pTail.setM_pNext(null);
			m_pTail = pTail;
		}
		else {
			m_pTail = pTail;
			m_pTail.setM_pNext(null);
			m_pTail.setM_pPrev(null);
			m_pHead = m_pTail;
		}
		m_nCount ++;
		
		if( m_nCount > stadafx.MAX_RECEIVE_BUFFER_SIZE) return RemoveHead();
		
		return null;
	}
	
	//특정 데이터 획득.
	public CUCareReceiveBuffer GetAt(long nNumber){
		if( nNumber >= m_nCount) return null;
		
		CUCareReceiveBuffer pAt = m_pHead;
		for(int k = 0; k < nNumber; k++)
			pAt = pAt.getM_pNext();
		
		return pAt;
	}
	
	//머리제거
	public CUCareReceiveBuffer RemoveHead(){
		CUCareReceiveBuffer pHead = m_pHead;
		
		if(m_pHead != null){
			m_pHead = m_pHead.getM_pNext();
			if(m_pHead != null){
				m_pHead.setM_pPrev(null);
			}
			pHead.setM_pNext(null);
			pHead.setM_pPrev(null);
			m_nCount--;
		}
		
		return pHead;
	}
	
	//꼬리 제거
	public CUCareReceiveBuffer RemoveTail(){
		CUCareReceiveBuffer pTail = m_pTail;
		if(m_pTail != null){
			m_pTail = m_pTail.getM_pPrev();
			if(m_pTail != null){
				m_pTail.setM_pNext(null);
			}
			pTail.setM_pNext(null);
			pTail.setM_pPrev(null);
		}
		
		return pTail;
	}
	
	//임의 대상 제거
	public CUCareReceiveBuffer RemoveAt(long nNumber){
		if(nNumber >= m_nCount)
			return null;
		
		CUCareReceiveBuffer pAt = GetAt(nNumber);
		CUCareReceiveBuffer pPrev = pAt.getM_pPrev();
		CUCareReceiveBuffer pNext = pAt.getM_pNext();
		
		if(pPrev != null) pPrev.setM_pNext(pNext);
		if(pNext != null) pNext.setM_pPrev(pPrev);
		
		pAt.setM_pNext(null);
		pAt.setM_pPrev(null);
		
		m_nCount--;
		
		return pAt;
	}
	
	public CUCareReceiveBuffer GetHead(){
		return m_pHead;
	}
	
	public CUCareReceiveBuffer GetTail(){
		return m_pTail;
	}
	
	public long GetCount(){
		return m_nCount;
	}
}
