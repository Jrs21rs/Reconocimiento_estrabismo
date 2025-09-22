import { Stack } from "expo-router";
import { AuthProvider, useAuth } from "../services/authContext";

export default function Layout() {
  return (
    <AuthProvider>
      <RootLayoutNav />
    </AuthProvider>
  );
}

function RootLayoutNav() {
  const { isAuthenticated } = useAuth();

  return (
    <Stack screenOptions={{ headerShown: false }}>
      {!isAuthenticated ? (
        <>
          <Stack.Screen name="index" />
          <Stack.Screen
            name="login"
            options={{
              presentation: 'modal'
            }}
          />
          <Stack.Screen
            name="register"
            options={{
              presentation: 'modal'
            }}
          />
        </>
      ) : (
        <>
          <Stack.Screen name="(tabs)" />
        </>
      )}
    </Stack>
  );
}
