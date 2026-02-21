import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api/api'

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' },
  card: { background: '#fff', padding: '40px', borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', width: '360px' },
  title: { fontSize: '24px', fontWeight: 'bold', marginBottom: '24px', textAlign: 'center' },
  label: { display: 'block', marginBottom: '4px', fontSize: '14px', color: '#555' },
  input: { width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '4px', fontSize: '14px', marginBottom: '16px' },
  button: { width: '100%', padding: '12px', background: '#2e7d32', color: '#fff', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer' },
  error: { color: 'red', fontSize: '13px', marginBottom: '12px' },
  success: { color: 'green', fontSize: '13px', marginBottom: '12px' },
  link: { textAlign: 'center', marginTop: '16px', fontSize: '14px' }
}

export default function RegisterPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '', name: '', phone: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await api.post('/api/auth/register', form)
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || '회원가입 실패')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>회원가입</h1>
        <form onSubmit={handleSubmit}>
          <label style={styles.label}>이름</label>
          <input
            style={styles.input}
            type="text"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="홍길동"
            required
          />
          <label style={styles.label}>이메일</label>
          <input
            style={styles.input}
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            placeholder="email@example.com"
            required
          />
          <label style={styles.label}>비밀번호</label>
          <input
            style={styles.input}
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            placeholder="8자 이상"
            required
          />
          <label style={styles.label}>전화번호</label>
          <input
            style={styles.input}
            type="text"
            value={form.phone}
            onChange={(e) => setForm({ ...form, phone: e.target.value })}
            placeholder="010-0000-0000"
          />
          {error && <div style={styles.error}>{error}</div>}
          <button style={styles.button} type="submit" disabled={loading}>
            {loading ? '처리 중...' : '회원가입'}
          </button>
        </form>
        <div style={styles.link}>
          이미 계정이 있으신가요? <Link to="/login">로그인</Link>
        </div>
      </div>
    </div>
  )
}
