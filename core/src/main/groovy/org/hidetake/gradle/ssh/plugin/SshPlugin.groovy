package org.hidetake.gradle.ssh.plugin

import groovy.util.logging.Slf4j
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.hidetake.groovy.ssh.Ssh
import org.hidetake.groovy.ssh.core.Proxy
import org.hidetake.groovy.ssh.core.Remote

/**
 * Main class of Gradle SSH plugin.
 *
 * @author Hidetake Iwata
 */
@Slf4j
class SshPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.ssh = Ssh.newService()
        project.extensions.remotes = createRemoteContainer(project)
        project.extensions.proxies = createProxyContainer(project)

        project.ssh.settings.logging = 'stdout'

        project.ssh.metaClass.mixin(VersionExtension)
    }

    private static createRemoteContainer(Project project) {
        def remotes = project.container(Remote)
        remotes.metaClass.mixin(RemoteContainerExtension)
        def parentRemotes = project.parent?.extensions?.findByName('remotes')
        if (parentRemotes instanceof NamedDomainObjectContainer<Remote>) {
            remotes.addAll(parentRemotes)
        }
        remotes
    }

    private static createProxyContainer(Project project) {
		def proxies = project.container(Proxy)
		def parentProxies = project.parent?.extensions?.findByName('proxies')
		if (parentProxies instanceof NamedDomainObjectContainer<Proxy>) {
			proxies.addAll(parentProxies)
		}
		proxies
    }
}
