// frontend/src/app/layout.tsx
import './globals.css'
import type { Metadata } from 'next'
import { ReactNode } from 'react'

export const metadata: Metadata = {
    title: 'My Next.js 13 App',
    description: 'An example using the app router',
}

export default function RootLayout({ children }: { children: ReactNode }) {
    return (
        <html lang="ko">
        <body>
        {/* 모든 페이지에 공통 적용될 레이아웃 */}
        {children}
        </body>
        </html>
    )
}
