import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/api'

const s = {
  page: { maxWidth: '1100px', margin: '0 auto', padding: '24px' },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' },
  title: { fontSize: '22px', fontWeight: 'bold' },
  logoutBtn: { padding: '8px 16px', background: '#d32f2f', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' },
  grid: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px' },
  card: { background: '#fff', borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', padding: '20px' },
  cardTitle: { fontSize: '16px', fontWeight: 'bold', marginBottom: '16px', color: '#333' },
  table: { width: '100%', borderCollapse: 'collapse' },
  th: { textAlign: 'left', padding: '8px', background: '#f5f5f5', borderBottom: '2px solid #ddd', fontSize: '13px' },
  td: { padding: '10px 8px', borderBottom: '1px solid #eee', fontSize: '14px' },
  buyBtn: { padding: '4px 12px', background: '#d32f2f', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '13px' },
  sellBtn: { padding: '4px 12px', background: '#1976d2', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '13px' },
  orderForm: { display: 'flex', flexDirection: 'column', gap: '12px' },
  select: { padding: '10px', border: '1px solid #ddd', borderRadius: '4px', fontSize: '14px' },
  input: { padding: '10px', border: '1px solid #ddd', borderRadius: '4px', fontSize: '14px' },
  row: { display: 'flex', gap: '8px' },
  submitBuy: { flex: 1, padding: '12px', background: '#d32f2f', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' },
  submitSell: { flex: 1, padding: '12px', background: '#1976d2', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' },
  msg: { padding: '10px', borderRadius: '4px', fontSize: '13px' },
  error: { background: '#ffebee', color: '#c62828' },
  success: { background: '#e8f5e9', color: '#2e7d32' },
  balance: { fontSize: '20px', fontWeight: 'bold', color: '#1976d2', marginBottom: '8px' },
  label: { fontSize: '13px', color: '#888' }
}

export default function DashboardPage() {
  const navigate = useNavigate()
  const [stocks, setStocks] = useState([])
  const [account, setAccount] = useState(null)
  const [order, setOrder] = useState({ stockCode: '', quantity: 1, price: '', orderType: 'BUY' })
  const [message, setMessage] = useState({ text: '', type: '' })
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      const [stocksRes, accountRes] = await Promise.all([
        api.get('/api/stocks'),
        api.get('/api/account')
      ])
      const stockList = stocksRes.data.data || stocksRes.data
      setStocks(Array.isArray(stockList) ? stockList : [])
      setAccount(accountRes.data.data || accountRes.data)
    } catch (err) {
      console.error('데이터 조회 실패', err)
    }
  }

  const handleQuickOrder = (stock, type) => {
    setOrder({ stockCode: stock.code, quantity: 1, price: stock.currentPrice, orderType: type })
    setMessage({ text: '', type: '' })
  }

  const handleOrderSubmit = async (type) => {
    if (!order.stockCode || !order.quantity || !order.price) {
      setMessage({ text: '종목, 수량, 가격을 입력해주세요.', type: 'error' })
      return
    }
    setLoading(true)
    setMessage({ text: '', type: '' })
    try {
      await api.post('/api/orders', {
        stockCode: order.stockCode,
        quantity: Number(order.quantity),
        price: Number(order.price),
        orderType: type
      })
      setMessage({ text: `${type === 'BUY' ? '매수' : '매도'} 주문이 완료되었습니다.`, type: 'success' })
      fetchData()
    } catch (err) {
      setMessage({ text: err.response?.data?.message || '주문 실패', type: 'error' })
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    navigate('/login')
  }

  const formatPrice = (price) => price?.toLocaleString('ko-KR') + '원'

  return (
    <div style={s.page}>
      <div style={s.header}>
        <span style={s.title}>모의투자 플랫폼</span>
        <button style={s.logoutBtn} onClick={handleLogout}>로그아웃</button>
      </div>

      <div style={s.grid}>
        {/* 주식 목록 */}
        <div style={s.card}>
          <div style={s.cardTitle}>주식 종목</div>
          <table style={s.table}>
            <thead>
              <tr>
                <th style={s.th}>종목명</th>
                <th style={s.th}>현재가</th>
                <th style={s.th}>거래</th>
              </tr>
            </thead>
            <tbody>
              {stocks.length === 0 ? (
                <tr><td colSpan={3} style={{ ...s.td, textAlign: 'center', color: '#999' }}>종목 없음</td></tr>
              ) : stocks.map((stock) => (
                <tr key={stock.code}>
                  <td style={s.td}>
                    <div>{stock.name}</div>
                    <div style={{ fontSize: '12px', color: '#999' }}>{stock.code}</div>
                  </td>
                  <td style={s.td}>{formatPrice(stock.currentPrice)}</td>
                  <td style={s.td}>
                    <div style={{ display: 'flex', gap: '4px' }}>
                      <button style={s.buyBtn} onClick={() => handleQuickOrder(stock, 'BUY')}>매수</button>
                      <button style={s.sellBtn} onClick={() => handleQuickOrder(stock, 'SELL')}>매도</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* 주문 + 계좌 */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* 계좌 정보 */}
          <div style={s.card}>
            <div style={s.cardTitle}>내 계좌</div>
            {account ? (
              <>
                <div style={s.label}>잔고</div>
                <div style={s.balance}>{formatPrice(account.balance)}</div>
                <div style={s.label}>총 자산</div>
                <div style={{ fontSize: '16px', fontWeight: 'bold' }}>{formatPrice(account.totalAssets)}</div>
              </>
            ) : (
              <div style={{ color: '#999', fontSize: '14px' }}>계좌 정보 없음</div>
            )}
          </div>

          {/* 주문 폼 */}
          <div style={s.card}>
            <div style={s.cardTitle}>주문</div>
            <div style={s.orderForm}>
              <select
                style={s.select}
                value={order.stockCode}
                onChange={(e) => setOrder({ ...order, stockCode: e.target.value })}
              >
                <option value="">종목 선택</option>
                {stocks.map((stock) => (
                  <option key={stock.code} value={stock.code}>
                    {stock.name} ({stock.code})
                  </option>
                ))}
              </select>
              <input
                style={s.input}
                type="number"
                placeholder="수량"
                min="1"
                value={order.quantity}
                onChange={(e) => setOrder({ ...order, quantity: e.target.value })}
              />
              <input
                style={s.input}
                type="number"
                placeholder="가격"
                min="1"
                value={order.price}
                onChange={(e) => setOrder({ ...order, price: e.target.value })}
              />
              {message.text && (
                <div style={{ ...s.msg, ...(message.type === 'error' ? s.error : s.success) }}>
                  {message.text}
                </div>
              )}
              <div style={s.row}>
                <button style={s.submitBuy} onClick={() => handleOrderSubmit('BUY')} disabled={loading}>
                  매수
                </button>
                <button style={s.submitSell} onClick={() => handleOrderSubmit('SELL')} disabled={loading}>
                  매도
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
