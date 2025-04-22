import pygame
from sys import exit


pygame.init()
screen = pygame.display.set_mode((736, 552))
pygame.display.set_caption("Plants Vs Zombies")
clock = pygame.time.Clock()

main_surf = pygame.image.load("Plants vs Zombies/Screen/MainScreen.jpg")

main_sound = pygame.mixer.Sound('Plants vs Zombies/Audio/Crazy Dave (Intro Theme).mp3')


while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit()
        screen.blit(main_surf, (0,0))
        main_sound.play()
        pygame.display.update()
        clock.tick(60)
