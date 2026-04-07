"use client";

import AppShell from "@/components/layout/AppShell";
import Button from "@/components/ui/Button";
import Input from "@/components/ui/Input";
import Card from "@/components/ui/Card";
import { useState , useEffect } from "react";
import { loginUser } from "@/lib/api";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

    useEffect(() => {
    setEmail("");
    setPassword("");
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {

      
      const res = await loginUser({ email, password });
console.log(res);
console.log(res.fullName);
      localStorage.setItem("accessToken", res.accessToken);
      localStorage.setItem("userId", res.userId);
      localStorage.setItem("email", res.email);
      localStorage.setItem("fullName", res.fullName);
      localStorage.setItem("roles", JSON.stringify(res.roles));
      localStorage.setItem("affiliation", ((res as any).affiliation ?? ""));
      localStorage.setItem("country", ((res as any).country ?? ""));
      router.push("/");
    } catch (err: any) {
      setError(err.message ?? "Login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <AppShell bare>
      <div className="flex h-full items-center justify-center">
        <Card className="w-full max-w-md">
          <h1 className="text-xl font-semibold tracking-tight">Login</h1>
          <p className="mt-1 text-sm text-slate-500">
            Mock authentication for the prototype.
          </p>
          <form className="mt-4 space-y-3" onSubmit={handleSubmit}>
            <Input
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <Input
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
                {error && (
        <div className="rounded-md bg-red-50 p-3 text-xs text-red-600 space-y-1">
          <p>No account found with these credentials.</p>
          <p>
            Don&apos;t have an account?{" "}
            <Link href="/auth/signup" className="font-medium underline underline-offset-2">
              Sign up here
            </Link>
          </p>
        </div>
      )}
            <Button type="submit" className="w-full" disabled={loading} >
              {loading ? "Signing in..." : "Sign in"}
            </Button>
          </form>
        </Card>
      </div>
    </AppShell>
  );
}

