# Vocary – Vocabulary Mastery

## Inspiration
Learning vocabulary is often repetitive and boring, making it difficult for learners to stay consistent. We wanted to create a tool that feels engaging, adaptive, and fun while still being effective. Inspired by spaced repetition systems (SRS), gamified apps, and the power of AI, we set out to build **Vocary** — a smarter way to master vocabulary.

## What it does
Vocary helps learners expand their English vocabulary through:
- **AI-generated word sets** based on level and topic.  
- **Spaced Repetition System (SRS)** to reinforce long-term retention.  
- **Interactive practice** with swipe-based learning and mini-games.  
- **Personal vocabulary collection** – search any word and add it to *My Own Words* for focused study.  
- **Progress tracking** with history, favorites, and **daily vocabulary targets**.  
- **Streak mode** – track learning streaks to boost motivation and consistency.  
- **Offline mode** – review and practice vocabulary anytime, even without internet access.  
- **Premium access** for extra topics and advanced features, managed seamlessly by RevenueCat.  

In short: Vocary makes vocabulary learning personalized, efficient, and enjoyable.

## How we built it
- **Frontend:** Jetpack Compose for a modern, responsive Android UI.  
- **Backend & Services:** Firebase Firestore for cloud data storage and Firebase Auth for user authentication.  
- **AI & NLP:** Azure AI for generating vocabulary sets and Azure Speech-to-Text for pronunciation practice.  
- **Notifications:** OneSignal for background vocabulary reminders when the app is inactive, to nudge users to practice daily.  
- **Offline storage:** Room Database for local caching, ensuring learning continues without internet.  
- **Monetization:** RevenueCat for subscription management and premium access control.  
- **Architecture:** Clean architecture with Repository pattern and reactive state management using coroutines & Flow.  

## Challenges we ran into
- Designing a **scalable data model** for dynamic word sets and progress tracking.  
- Making the UI **smooth and responsive** despite frequent state updates (SRS, filters, dynamic forms).  
- Integrating **multiple APIs** while handling rate limits and inconsistent data formats.  
- Ensuring **real-time sync** between Firestore and local Room database.  
- Implementing **subscription logic** that feels natural and non-intrusive.  

## Accomplishments that we're proud of
- Built a fully functional **AI-assisted vocabulary trainer** in a short time.  
- Integrated **RevenueCat** seamlessly for premium access.  
- Achieved a **clean, reusable UI** with Jetpack Compose.  
- Created a **scalable system** for SRS and progress tracking.  
- Designed an experience that feels both **educational and fun**.  

## What we learned
- How to integrate **RevenueCat** effectively for subscriptions.  
- Best practices in **modern Android development** with Jetpack Compose.  
- Importance of **clean architecture** when managing multiple data sources (AI, Firestore, Room).  
- How **gamification and SRS** can significantly improve language learning engagement.  
- The value of testing in **real-world scenarios** to refine UX and performance.  
- Implementing **push notifications** with OneSignal to remind users and boost daily practice.  

## What's next for Vocary - Vocabulary Mastery
- **Expand to more languages** beyond English.  
- **Add voice practice** with pronunciation feedback.  
- **Build AI-powered writing & speaking feedback** – short text evaluation, conversation partner mode, fluency and grammar checks.  
- **Enhance personalization** with AI-driven study plans.  
- **Develop social & community features** – leaderboards, friend challenges, shared word lists, and discussion boards.  
- **Add more mini-games** such as word puzzles, timed quizzes, crosswords, and matching games.  
- **Build an analytics dashboard** to visualize progress, strengths, and weak areas.  
- **Develop integrations with external dictionaries & translators** for richer learning context.  
- **Introduce advanced streak & reward features** – more detailed streak tracking, badges, and incentives.  
- **Prepare app for a wider launch** with full premium support.  

