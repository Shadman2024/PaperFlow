"use client";

import AppShell from "@/components/layout/AppShell";
import Card from "@/components/ui/Card";
import Input from "@/components/ui/Input";
import Button from "@/components/ui/Button";
import { useState } from "react";
import { submitPaper } from "@/lib/api";
import { useParams } from "next/navigation";

export default function SubmitPaperPage() {
  const params = useParams<{ id: string }>();
  const conferenceId = params.id;

  const [file , setFile] = useState<File | null>(null);
  const [form, setForm] = useState({
    title: "",
    abstract: "",
    track: ""
  });
  const [message, setMessage] = useState<string | null>(null);
async function handleSubmit(e: React.FormEvent) {
  e.preventDefault();

  console.log("🚀 Submit clicked");
  console.log("conferenceId:", conferenceId);
  console.log("form:", form);
  console.log("file:", file);

  if (!file) {
    setMessage("Please select a PDF file");
    return;
  }

  const formData = new FormData();
  formData.append("conferenceId", conferenceId);
  formData.append("title", form.title);
  formData.append("abstractText", form.abstract);
  formData.append("track", form.track);
  formData.append("file", file);

  console.log("📦 FormData ready");

  try {
    const res = await submitPaper(formData);
    console.log("✅ Response:", res);
    setMessage(`Submitted paper ${res.paperId} (${res.status})`);
  } catch (err: any) {
    console.log("❌ ERROR:", err);
    setMessage(err.message ?? "Failed to submit paper");
  }
}
  return (
    <AppShell>
      <h1 className="text-2xl font-semibold tracking-tight">
        Submit paper
      </h1>
      <Card className="mt-4 max-w-2xl space-y-3">
        <form className="space-y-3" onSubmit={handleSubmit}>
          <Input
            label="Title"
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
            required
          />
          <label className="flex flex-col gap-1 text-sm">
            <span className="font-medium text-slate-700">Abstract</span>
            <textarea
              className="min-h-[120px] rounded-md border border-slate-200 bg-white px-3 py-2 text-sm text-slate-900 shadow-sm focus:border-slate-500 focus:outline-none focus:ring-2 focus:ring-slate-400"
              value={form.abstract}
              onChange={(e) =>
                setForm({ ...form, abstract: e.target.value })
              }
            />
          </label>
          <Input
            label="Track"
            value={form.track}
            onChange={(e) => setForm({ ...form, track: e.target.value })}
          />

                 <label className="flex flex-col gap-1 text-sm">
  <span className="font-medium text-slate-700">Upload PDF</span>
  <input
    type="file"
    accept="application/pdf"
    onChange={(e) => {
      if (e.target.files && e.target.files[0]) {
        setFile(e.target.files[0]);
      }
    }}
  />
</label>
          {message && (
            <p className="text-xs text-slate-600">
              {message}
            </p>
          )}
          <Button type="submit">Submit</Button>
        </form>
      </Card>
    </AppShell>
  );
}

