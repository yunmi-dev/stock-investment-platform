import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api/api'

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' },
  card: { background: '#fff', padding: '40px', borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', width: '360px' },
  title: { fontSize: '24px', fontWeight: 'bold', marginBottom: '24px', textAlign: 'center' },
  label: { display: 'block', marginBottom: '4px', fontSize: '14px', color: '#555' },
  input: { width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '4px', fontSize: '14px', marginBottom: '16px' },
  button: { width: '100%', padding: '12px', background: '#1976d2', color: '#fff', border: 'none', borderRadius: '4px', fontSize: '16px', cursor: 'pointer' },
  error: { color: 'red', fontSize: '13px', marginBottom: '12px' },
  link: { textAlign: 'center', marginTop: '16px', fontSize: '14px' }
}

export default function LoginPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await api.post('/api/auth/login', form)
      localStorage.setItem('token', res.data.data?.token || res.data.token)
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message || '로그인 실패')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>모의투자 플랫폼</h1>
        <form onSubmit={handleSubmit}>
          <label style={styles.label}>이메일</label>
          <input
            style={styles.input}
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            placeholder="test@test.com"
            required
          />
          <label style={styles.label}>비밀번호</label>
          <input
            style={styles.input}
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            placeholder="비밀번호"
            required
          />
          {error && <div style={styles.error}>{error}</div>}
          <button style={styles.button} type="submit" disabled={loading}>
            {loading ? '로그인 중...' : '로그인'}
          </button>
        </form>
        <div style={styles.link}>
          계정이 없으신가요? <Link to="/register">회원가입</Link>
        </div>
      </div>
    </div>
  )
}
