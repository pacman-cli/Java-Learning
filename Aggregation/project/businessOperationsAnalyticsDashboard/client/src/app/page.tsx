import { FloatingParticles } from "@/components/landing/FloatingParticles"
import { Footer } from "@/components/landing/Footer"
import { Hero } from "@/components/landing/Hero"
import { HowItWorks } from "@/components/landing/HowItWorks"
import { Navbar } from "@/components/landing/Navbar"
import { ScrollStack } from "@/components/landing/ScrollStack"

export default function LandingPage() {
  return (
    <div className= "min-h-screen bg-background text-foreground font-sans selection:bg-primary/30" >
    <Navbar />
    < FloatingParticles />
    <Hero />
    < ScrollStack />
    <HowItWorks />

  {/* Premium CTA Section */ }
  <section className="py-32 px-4 text-center relative overflow-hidden" >
    {/* Glow Background */ }
    < div className = "absolute inset-0 -z-10 bg-gradient-to-t from-primary/5 to-transparent" />
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-primary/10 rounded-full blur-[100px] pointer-events-none" />

        <div className="container mx-auto max-w-3xl relative z-10" >
          <h2 className="text-4xl md:text-6xl font-bold tracking-tight mb-8" >
            Start exploring your data today.
          </h2>
              < p className = "text-muted-foreground mb-10 text-xl max-w-lg mx-auto" >
                Join the new standard of business analytics.Free for 14 days, no credit card required.
          </ p >
                  < a
  href = "/register"
  className = "inline-flex h-14 items-center justify-center rounded-full bg-primary px-10 text-lg font-medium text-primary-foreground shadow-xl shadow-primary/30 transition-all hover:scale-105 active:scale-95 hover:bg-primary/90"
    >
    Get Started Now
      </ a >
      </div>
      </section>

      < Footer />
      </div>
  );
}
