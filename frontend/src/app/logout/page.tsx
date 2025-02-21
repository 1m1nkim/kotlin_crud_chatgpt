"use client";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function LogoutPage() {
    const router = useRouter();

    useEffect(() => {
        async function logout() {
            try {
                // 백엔드 로그아웃 API 호출
                const res = await fetch("http://localhost:8080/api/auth/logout", {
                    method: "POST",
                    credentials: "include", // 쿠키 포함
                });
                if (res.ok) {
                    // 로그아웃 성공 시 로그인 페이지로 이동
                    router.push("/login");
                } else {
                    console.error("로그아웃 실패");
                }
            } catch (error) {
                console.error("로그아웃 요청 중 에러 발생", error);
            }
        }
        logout();
    }, [router]);

    return (
        <main style={{ padding: "20px" }}>
            <h1>로그아웃 중입니다...</h1>
        </main>
    );
}
