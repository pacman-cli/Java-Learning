"use client"

import { AuthButton } from "@/components/auth/AuthButton"
import { AuthInput } from "@/components/auth/AuthInput"
import { AuthLayout } from "@/components/auth/AuthLayout"
import { Lock, Mail, User } from "lucide-react"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { useState } from "react"

export default function RegisterPage() {
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
      title= "Create Account"
  subtitle = "Join thousands of businesses growing with BusinessKPI."
    >
    <form onSubmit={ handleSubmit } className = "space-y-4" >
      <AuthInput
            label="Full Name"
  type = "text"
  placeholder = "John Doe"
  icon = { User }
  required
    />

    <AuthInput
            label="Email Address"
  type = "email"
  placeholder = "name@example.com"
  icon = { Mail }
  required
    />

    <AuthInput
            label="Password"
  type = "password"
  placeholder = "Create a password"
  icon = { Lock }
  required
    />

    <AuthInput
            label="Confirm Password"
  type = "password"
  placeholder = "Confirm your password"
  icon = { Lock }
  required
    />

    <div className="pt-2" >
      <AuthButton type="submit" isLoading = { isLoading } >
        Create Account
          </AuthButton>
          </div>

          < div className = "text-center text-sm text-muted-foreground pt-2" >
            Already have an account ? { " "}
              < Link href = "/login" className = "text-primary font-medium hover:underline" >
                Sign In
                  </ Link >
                  </div>
                  </form>
                  </AuthLayout>
  );
}
