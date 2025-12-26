"use client"

import { AuthButton } from "@/components/auth/AuthButton"
import { AuthInput } from "@/components/auth/AuthInput"
import { AuthLayout } from "@/components/auth/AuthLayout"
import { Lock, Mail } from "lucide-react"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { useState } from "react"

export default function LoginPage() {
  const router = useRouter()
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    // Mock API call
    await new Promise((resolve) => setTimeout(resolve, 1500))
    router.push("/dashboard")
  }

  return (
    <AuthLayout
      title="Welcome Back"
      subtitle="Enter your credentials to access your account."
    >
      <form onSubmit={handleSubmit} className="space-y-4" >
        <AuthInput
          label="Email Address" type="email"
          placeholder="name@example.com"
          icon={Mail}
          required
        />

        <div className="space-y-1" >
          <AuthInput
            label="Password"
            type="password"
            placeholder="Enter your password"
            icon={Lock}
            required
          />
          <div className="flex justify-end" >
            <Link href="#" className="text-xs text-primary font-medium hover:underline" >
              Forgot password ?
            </Link>
          </div>
        </div>

        < div className="pt-2" >
          <AuthButton type="submit" isLoading={isLoading} >
            Sign In
          </AuthButton>
        </ div>

        < div className="text-center text-sm text-muted-foreground pt-2" >
          Don & apos;t have an account ? {" "}
          < Link href="/register" className="text-primary font-medium hover:underline" >
            Register
          </Link>
        </div>
      </form>
    </AuthLayout>
  )
}
