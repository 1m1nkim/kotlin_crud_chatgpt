// frontend/src/app/page.tsx
import Link from 'next/link'

export default function HomePage() {
    return (
        <main style={{ padding: '20px' }}>
            <h1>홈페이지</h1>
            <p>여기는 루트(/) 페이지입니다.</p>
            <Link href="/login" style={{ marginTop: '10px', display: 'inline-block' }}>
                로그인 페이지로 이동
            </Link>
        </main>
    )
}
