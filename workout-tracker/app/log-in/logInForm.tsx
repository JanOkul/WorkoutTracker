"use client";


export default function LogInForm() {
  async function submitLogin(e) {
    const request = await fetch("api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email: e.target.email.value,
        password: e.target.password.value
      })
    })

    const data = await request.json () 
  }

  return (
    <form action={submitLogin}>
      <input name="email" />
      <input type="password" name="password" />
      <button type="submit">Log In</button>
    </form>
  )
}
