// frontend/src/app/login/page.tsx

export default function LoginPage() {
    // 백엔드 도메인 설정
    const backendUrl = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080'

    // 카카오 OAuth2 엔드포인트
    const kakaoLoginUrl = `${backendUrl}/oauth2/authorization/kakao`

    return (
        <main style={{ padding: '20px' }}>
            <h1>로그인 페이지</h1>
            <p>카카오 로그인 테스트</p>
            <a
                href={kakaoLoginUrl}
                style={{
                    display: 'inline-block',
                    marginTop: '10px',
                    padding: '10px 20px',
                    backgroundColor: '#FEE500',
                    color: '#000',
                    borderRadius: '4px',
                    fontWeight: 'bold',
                }}
            >
                카카오로 로그인
            </a>
        </main>
    )
}
