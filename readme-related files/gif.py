import imageio


def create_gif(image_list, gif_name, duration=1.0):
    frames = []
    for image_name in image_list:
        frames.append(imageio.imread(image_name))

    imageio.mimsave(gif_name, frames, 'GIF', duration=duration)
    return


def main():
    image_list = []
    for i in range(1, 20):
        image_list.append("{}.jpg".format(i))
    gif_name = 'new.gif'
    duration = 0.5
    create_gif(image_list, gif_name, duration)


if __name__ == '__main__':
    main()
