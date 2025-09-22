import { Stack } from "expo-router";
import { useAuth } from "../../services/authContext";

export default function Layout() {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return null;
  }

  return (
    <Stack>
      <Stack.Screen 
        name="home" 
        options={{ 
          title: "Inicio",
          headerShown: true,
          headerBackVisible: false,
          headerStyle: {
            backgroundColor: '#4c669f',
          },
          headerTintColor: '#fff',
          headerTitleStyle: {
            fontWeight: 'bold',
          },
        }} 
      />
    </Stack>
  );
}
