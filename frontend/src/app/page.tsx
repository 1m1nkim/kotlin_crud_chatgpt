"use client";
import Link from 'next/link';
import { useState, useEffect } from 'react';

export default function HomePage() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // 예: 쿠키에 accessToken이 있는지 확인하거나,
        //     백엔드 API를 호출하여 로그인 여부를 체크하는 로직
        const hasToken = document.cookie.includes('accessToken=');
        setIsLoggedIn(hasToken);
    }, []);

    return (
        <main style={{ padding: '20px' }}>
            <h1>홈페이지</h1>
            <p>여기는 루트(/) 페이지입니다.</p>

            {isLoggedIn ? (
                <Link href="/logout" style={{ marginTop: '10px', display: 'inline-block' }}>
                    로그아웃 페이지로 이동
                </Link>
            ) : (
                <Link href="/login" style={{ marginTop: '10px', display: 'inline-block' }}>
                    로그인 페이지로 이동
                </Link>
            )}
        </main>
    );
}
